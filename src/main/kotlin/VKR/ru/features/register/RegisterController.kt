package VKR.ru.features.register


import VKR.ru.database.disciplinelists.Disciplinelist
import VKR.ru.database.disciplinelists.DisciplinelistDTO
import VKR.ru.database.disciplines.Discipline
import VKR.ru.database.disciplines.DisciplineDTO
import VKR.ru.database.facultys.Faculty
import VKR.ru.database.facultys.FacultyDTO
import VKR.ru.database.groups.GroupDTO
import VKR.ru.database.groups.Groups
import VKR.ru.database.roles.Role
import VKR.ru.database.roles.Roles
import VKR.ru.database.roles.RolesDTO
import VKR.ru.database.teachers.Student
import VKR.ru.database.teachers.StudentDTO
import VKR.ru.database.users.UsersDTO
import VKR.ru.database.users.Users
import VKR.ru.features.login.LoginPassRemote
import VKR.ru.utils.GenUniqueIdDiscipline
import VKR.ru.utils.GenUniqueIdFaculty
import VKR.ru.utils.GenUniqueIdGroup
import VKR.ru.utils.GenUniqueIdUser
import VKR.ru.utils.TokenCheck
import VKR.ru.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.Base64


class RegisterController (private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
            if (!registerReceiveRemote.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }

            if (!registerReceiveRemote.password.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Password is not valid")
            }

            val usersDTO = Users.fetchUser(registerReceiveRemote.login)

            if (usersDTO != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } else {
                val idUser = GenUniqueIdUser.generateRandomIDUser()
                try {
                    Users.insert(
                        UsersDTO(
                            login = registerReceiveRemote.login,
                            password = registerReceiveRemote.password,
                            email = registerReceiveRemote.email,
                            idUser = idUser,
                            name = registerReceiveRemote.name
                        )
                    )
                    Roles.insert(
                        RolesDTO(
                            idRoles = idUser,
                            role = registerReceiveRemote.role
                        )
                    )
                    val login = registerReceiveRemote.login
                    val password = registerReceiveRemote.password
                    val loginAndPassword = "$login:$password"
                    val encodedString = Base64.getEncoder().encodeToString(loginAndPassword.toByteArray())
                    call.respond(encodedString)
                } catch (e: ExposedSQLException) {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Can't create user ${e.localizedMessage}"
                    )
                }
            }
        }   else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun registerNewStudent() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val registerReceiveRemote = call.receive<RegisterStudentReceiveRemote>()
            if (!registerReceiveRemote.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
                return
            }

            val usersDTO = Users.fetchUser(registerReceiveRemote.login)
            if (usersDTO != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
                return
            }

            val idUser = GenUniqueIdUser.generateRandomIDUser()

            // Check if Faculty already exists
            val existingFaculty = Faculty.fetchByName(registerReceiveRemote.FacultyName)
            val idFaculty = if (existingFaculty != null) {
                existingFaculty.idFaculty
            } else {
                GenUniqueIdFaculty.generateRandomIdFaculty()
            }

            // Check if Group already exists
            val existingGroup = Groups.fetchByName(registerReceiveRemote.GroupName)
            var idGroup = if (existingGroup != null && existingGroup.nameGroup == registerReceiveRemote.GroupName) {
                existingGroup.idGroup
            } else {
                GenUniqueIdGroup.generateRandomIdGroup()
            }

            try {
                Users.insert(
                    UsersDTO(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password,
                        email = registerReceiveRemote.email,
                        idUser = idUser,
                        name = registerReceiveRemote.name
                    )
                )
                Roles.insert(
                    RolesDTO(
                        idRoles = idUser,
                        role = Role.STUDENT
                    )
                )

                // Insert Faculty if it doesn't exist
                if (existingFaculty == null) {
                    Faculty.insert(
                        FacultyDTO(
                            idFaculty = idFaculty,
                            nameFaculty = registerReceiveRemote.FacultyName
                        )
                    )
                }

                // Insert Group if it doesn't exist
                if (existingGroup == null) {
                    Groups.insert(
                        GroupDTO(
                            idGroup = idGroup,
                            nameGroup = registerReceiveRemote.GroupName,
                            course = registerReceiveRemote.course,
                            faculty_id = idFaculty
                        )
                    )
                }

                Student.insert(
                    StudentDTO(
                        idStudent = idUser,
                        idGroup = idGroup
                    )
                )

                val login = registerReceiveRemote.login
                val password = registerReceiveRemote.password
                val loginAndPassword = "$login:$password"
                val encodedString = Base64.getEncoder().encodeToString(loginAndPassword.toByteArray())
                call.respond(encodedString)
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Can't create user ${e.localizedMessage}"
                )
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun registerNewTeacher() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val registerReceiveRemote = call.receive<RegisterTeacherReceiveRemote>()
            if (!registerReceiveRemote.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }
            val usersDTO = Users.fetchUser(registerReceiveRemote.login)
                if (usersDTO != null) {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                } else {
                    val idUser = GenUniqueIdUser.generateRandomIDUser()
                    val idDisciplines = registerReceiveRemote.nameDisciplines.map { GenUniqueIdDiscipline.generateRandomIdDiscipline() }
                    try {
                        Users.insert(
                            UsersDTO(
                                login = registerReceiveRemote.login,
                                password = registerReceiveRemote.password,
                                email = registerReceiveRemote.email,
                                idUser = idUser,
                                name = registerReceiveRemote.name
                            )
                        )
                        Roles.insert(
                            RolesDTO(
                                idRoles = idUser,
                                role = Role.TEACHER
                            )
                        )
                        for (nameDiscipline in registerReceiveRemote.nameDisciplines) {
                            val idDiscipline = idDisciplines[registerReceiveRemote.nameDisciplines.indexOf(nameDiscipline)]

                            val disciplineDTO = DisciplineDTO(
                                idDiscipline = idDiscipline,
                                nameDiscipline = nameDiscipline
                            )
                            Discipline.insert(disciplineDTO)
                            val disListDTO = DisciplinelistDTO(
                                teacherId = idUser,
                                disciplineId = idDiscipline
                            )
                            Disciplinelist.insert(disListDTO)
                        }
                    } catch (e: ExposedSQLException) {
                        call.respond(HttpStatusCode.Conflict, "User already exists")
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "Can't create user ${e.localizedMessage}"
                        )
                    }
                }
                val login = registerReceiveRemote.login
                val password = registerReceiveRemote.password
                val loginAndPassword = "$login:$password"
                val encodedString = Base64.getEncoder().encodeToString(loginAndPassword.toByteArray())
                call.respond(encodedString)
            } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}
