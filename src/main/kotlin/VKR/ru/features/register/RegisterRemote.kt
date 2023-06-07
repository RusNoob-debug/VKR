package VKR.ru.features.register

import VKR.ru.database.roles.Role

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val login: String,
    val email: String,
    val password: String,
    val name: String,
    val role: Role
)


@Serializable
data class RegisterStudentReceiveRemote(
    val login: String,
    val email: String,
    val password: String,
    val name: String,
    val FacultyName: String,
    val GroupName: String,
    val course: Byte,
)

@Serializable
data class RegisterTeacherReceiveRemote(
    val login: String,
    val email: String,
    val password: String,
    val name: String,
    val nameDisciplines: List<String>
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)