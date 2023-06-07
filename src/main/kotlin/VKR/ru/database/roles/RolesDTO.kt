package VKR.ru.database.roles

import kotlinx.serialization.Serializable

import java.time.Duration

enum class Role {
    TEACHER,
    STUDENT
}

class RolesDTO(
    val idRoles: Long,
    val role: Role
)