package VKR.ru.database.users

import VKR.ru.features.groups.models.IdStudentResponse


class UsersDTO (
    val idUser: Long,
    val login: String,
    val password: String,
    val email: String,
    val name: String,
    )

fun UsersDTO.mapToIdResponseDTO(): IdStudentResponse {
    return IdStudentResponse(
        id = idUser,
        name = name,
        email = email,
    )
}
