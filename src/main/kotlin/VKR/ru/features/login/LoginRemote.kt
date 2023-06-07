package VKR.ru.features.login


import VKR.ru.database.roles.Role
import kotlinx.serialization.Serializable


@Serializable
data class LoginReceiveRemote(
    val X_AUTHENTICATION : String,
)

@Serializable
data class LoginPassRemote(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponseRemote(
    val id: Long,
    val authorization_token: String,
    val role: Role
)


