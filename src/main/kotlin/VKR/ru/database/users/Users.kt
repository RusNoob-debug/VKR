package VKR.ru.database.users

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Users: Table() {
    private val login = varchar("login", 50)
    private val password = varchar("password", 25)
    private val idUser = long("idUser")
    private val email = varchar("email", 25)
    private val name = varchar("name", 100)

    fun insert (usersDTO: UsersDTO)
    { transaction {
       Users.insert {
            it[login] = usersDTO.login
            it[password] = usersDTO.password
            it[email] = usersDTO.email
            it[idUser] = usersDTO.idUser
            it[name] = usersDTO.name
        }
     }
    }

   fun returnIdUserByLogin(login: String): Long? {
        return try {
            transaction {
                Users.select {
                    Users.login eq login
                }.singleOrNull()?.get(Users.idUser)
            }
        } catch (e: Exception) {
            null
        }
    }


    fun fetchAll(): List<UsersDTO> {
        return try {
            transaction {
                Users.selectAll().toList()
                    .map {
                        UsersDTO(
                            login = it[Users.login],
                            password = it[Users.password],
                            email = it [Users.email],
                            idUser = it[Users.idUser],
                            name = it[Users.name],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchUser (login: String): UsersDTO? {
        return try {
            transaction {
                val usersModel = Users.select { Users.login.eq(login) }.single()
                UsersDTO(
                    login = usersModel[Users.login],
                    password = usersModel[password],
                    email = usersModel [email],
                    idUser = usersModel[idUser],
                    name = usersModel[name],
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun findUserIdByLogin(login: String): Long {
        return try {
            transaction {
                val userRow = Users.select { Users.login.eq(login) }.singleOrNull()
                userRow?.let { it[Users.idUser] } ?: error("User not found") // Возвращает значение idUser, если пользователь найден, иначе генерирует ошибку
            }
        } catch (e: Exception) {
            error("")
        }
    }

    fun hasMatchingIdUser(idUser: Long): Boolean {
        return try {
            transaction {
                Users.select {
                    Users.idUser eq idUser
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchById(idUser: Long): UsersDTO? {
        return try {
            transaction {
                Users.select {
                    Users.idUser eq idUser
                }.mapNotNull {
                    UsersDTO(
                        login = it[Users.login],
                        password = it[Users.password],
                        email = it[Users.email],
                        idUser = it[Users.idUser],
                        name = it[Users.name],
                    )
                }.singleOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }


    fun deleteById(idUser: Long): Boolean {
        return try {
            transaction {
                val deletedRowCount = Users.deleteWhere { Users.idUser eq idUser }
                deletedRowCount > 0
            }
        } catch (e: Exception) {
            false
        }
    }


    fun fetchByLogin(login: String): UsersDTO? {
        return try {
            transaction {
                Users.select {
                    Users.login eq login
                }.singleOrNull()?.let {
                    UsersDTO(
                        login = it[Users.login],
                        password = it[Users.password],
                        email = it[Users.email],
                        idUser = it[Users.idUser],
                        name = it[Users.name]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }


}
