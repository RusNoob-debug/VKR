package VKR.ru.features.register

import VKR.ru.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRegisterRouting() {
    routing {
        post ("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }
    }
    routing {
        post ("/register/student") {
            val registerController = RegisterController(call)
            registerController.registerNewStudent()
        }
    }
    routing {
        post ("/register/teacher") {
            val registerController = RegisterController(call)
            registerController.registerNewTeacher()
        }
    }
}




