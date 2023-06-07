package VKR.ru.database.roles

import VKR.ru.database.users.Users
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Roles : Table() {
    private val idRoles = long("idRoles")
    private val role = varchar("role", 25)

    fun insert(RolesDTO: RolesDTO) {
        transaction {
            Roles.insert {
                it[idRoles] = RolesDTO.idRoles
                it[role] = RolesDTO.role.name // Присваиваем значение имени перечисления
            }
        }
    }

    fun findRoleById(id: Long): Role {
        return try {
            transaction {
                val roleRow = Roles.select { Roles.idRoles eq id }.singleOrNull()
                roleRow?.let { Role.valueOf(it[Roles.role]) } ?: error("Role not found") // Возвращает Role, если роль с заданным id найдена, иначе генерирует ошибку
            }
        } catch (e: Exception) {
            error("")
        }
    }

    fun hasRoleByIdRole(id: Long): Boolean {
        return try {
            transaction {
                Roles.select {
                    Roles.idRoles eq id and (Roles.role eq Role.TEACHER.name)
                }.any()
            }
        } catch (e: Exception) {
            false
        }
    }
}
