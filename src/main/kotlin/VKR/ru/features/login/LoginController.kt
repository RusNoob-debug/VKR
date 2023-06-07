package VKR.ru.features.login

import VKR.ru.database.ipsecurity.FailedLoginAttempts
import VKR.ru.database.ipsecurity.IpAddress
import VKR.ru.database.ipsecurity.IpAddressDTO
import VKR.ru.database.roles.Roles
import VKR.ru.database.tokens.TokenDTO
import VKR.ru.database.tokens.Tokens
import VKR.ru.database.users.Users
import VKR.ru.utils.GenUniqueIdToken
import VKR.ru.utils.GenUniqueToken
import VKR.ru.utils.Headers
import io.ktor.http.*
import io.ktor.server.response.*
import java.util.*
import java.util.TimerTask
import kotlin.random.Random
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.server.application.ApplicationCall
import org.jetbrains.exposed.sql.transactions.*

class TokenCleanupTask : TimerTask() {
    override fun run() {
        transaction {
            Tokens.deleteExpiredTokens()
            IpAddress.deleteExpiredIpAddresses()
        }
    }
}


class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {

        val rec = call.request.headers[Headers.X_AUTHENTICATION]

        fun decodeAuthData(encodedData: String): Pair<String, String>? {
            val decodedBytes = Base64.getDecoder().decode(encodedData)
            val decodedString = String(decodedBytes)
            val splitData = decodedString.split(":")
            if (splitData.size == 2) {
                val login = splitData[0]
                val password = splitData[1]
                return login to password
            }
            return null
        }

        val authData = rec?.let { decodeAuthData(it) }
        val receive = LoginPassRemote(login = authData?.first ?: "", password = authData?.second ?: "")

        val usersDTO = transaction { Users.fetchUser(receive.login) }
        val ipAddress = getPublicIPAddress()
        if (usersDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "Wrong login")
            recordFailedLoginAttempt(call, ipAddress)
        } else {
            if (ipAddress != null && isIpAddressBlocked(ipAddress)) {
                call.respondWithError(HttpStatusCode.BadRequest, "Wrong login")
                recordFailedLoginAttempt(call, ipAddress)
            } else {
                if (usersDTO.password == receive.password) {
                    val token = GenUniqueToken.generateUniqueToken()
                    val idToken = GenUniqueIdToken.generateUniqueIdToken()

                    val idUser = Users.findUserIdByLogin(receive.login)
                    val roleUser = Roles.findRoleById(idUser)

                    transaction {
                        Tokens.insert(
                            TokenDTO(
                                expiration = System.currentTimeMillis() + 14400_000L,
                                idToken = GenUniqueIdToken.generateUniqueIdToken(),
                                login = receive.login,
                                token = token
                            )
                        )
                    }
                    call.respond(LoginResponseRemote(id = idToken,authorization_token = token, role = roleUser))
                    // Добавьте следующую строку для обработки успешной попытки входа после неудачных попыток
                    FailedLoginAttempts.resetFailedAttempts(ipAddress!!)
                } else {
                    call.respondWithError(HttpStatusCode.BadRequest, "Wrong passwords")
                    recordFailedLoginAttempt(call, ipAddress)
                }
            }
        }
    }

    private suspend fun recordFailedLoginAttempt(call: ApplicationCall, ipAddress: String?) {
        if (ipAddress != null) {
            val failedAttempts = FailedLoginAttempts.getFailedAttempts(ipAddress)
            FailedLoginAttempts.incrementFailedAttempts(ipAddress)

            if (failedAttempts >= 2) { // Проверяем, что это третья неудачная попытка (т.к. счет начинается с 0)
                val ipExists = transaction { IpAddress.fetchIpAddresses(ipAddress) }
                if (!ipExists) {
                    transaction {
                        IpAddress.insert(
                            IpAddressDTO(
                                idAddress = Random.nextLong(),
                                ipAddress = ipAddress,
                                timestamp = System.currentTimeMillis() + 300_000L
                            ),
                            call.request
                        )
                    }
                }
            }
        } else {
            call.respondWithError(HttpStatusCode.BadRequest, "Wrong passwords")
        }
    }

    private val client = HttpClient()

    private suspend fun getPublicIPAddress(): String? {
        return try {
            client.use {
                val response: HttpResponse = it.get("https://api64.ipify.org")
                response.bodyAsText()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isIpAddressBlocked(ipAddress: String?): Boolean {
        return ipAddress != null && transaction { IpAddress.fetchIpAddresses(ipAddress) }
    }

    private suspend fun ApplicationCall.respondWithError(status: HttpStatusCode, message: String) {
        respond(status, message)
    }
}