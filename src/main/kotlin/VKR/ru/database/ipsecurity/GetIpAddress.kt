package VKR.ru.database.ipsecurity

import io.ktor.server.request.ApplicationRequest
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object IpAddress : Table() {
    private val idAddress = long("idAddress")
    private val ipAddress = varchar("ipAddress", 500).nullable()
    private val timestamp = long("timestamp")

    fun insert(ipAddressDTO: IpAddressDTO, request: ApplicationRequest) {
        transaction {
            IpAddress.insert {
                it[idAddress] = ipAddressDTO.idAddress
                it[ipAddress] = ipAddressDTO.ipAddress
                it[timestamp] = ipAddressDTO.timestamp
            }
        }
    }

    fun deleteExpiredIpAddresses() {
        transaction {
            IpAddress.deleteWhere { timestamp lessEq System.currentTimeMillis() }
        }
    }

    // Метод, ipAddress из таблицы
    fun fetchIpAddresses(ip: String): Boolean {
        return transaction {
            IpAddress.select { IpAddress.ipAddress eq ip }
                .singleOrNull()?.getOrNull(IpAddress.ipAddress) != null
        }
    }
}

object FailedLoginAttempts {
    private val attemptsMap: MutableMap<String, Int> = mutableMapOf()

    fun incrementFailedAttempts(ipAddress: String) {
        val attempts = attemptsMap.getOrDefault(ipAddress, 0) + 1
        attemptsMap[ipAddress] = attempts
    }

    fun getFailedAttempts(ipAddress: String): Int {
        return attemptsMap[ipAddress] ?: 0
    }

    fun resetFailedAttempts(ipAddress: String) {
        attemptsMap.remove(ipAddress)
    }
}