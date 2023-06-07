package VKR.ru.service


import VKR.ru.features.student.StudentController
import VKR.ru.utils.TokenCheck
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureUserService() {
    routing {
        get("/user/student") {
            val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
            if (TokenCheck.isTokenAdmin(token.orEmpty()) || !TokenCheck.isTokenTeacher(token.orEmpty())) {
                val students = StudentController(call).getListStudentForToken(token)
                call.respond(students)
            }
        }
    }

    routing {
        get("/user/teacher") {
            val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
            if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
                val students = StudentController(call).getTeacherDisciplinesByToken(token)
                call.respond(students)
            }
        }
    }

}