package VKR.ru.routing

import VKR.ru.features.department.DepartmentController
import VKR.ru.features.department.FacultyController
import VKR.ru.features.department.RoomController
import VKR.ru.features.groups.GroupController
import VKR.ru.features.student.StudentController
import VKR.ru.features.student.models.FetchStudentRequestList
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureUniversityService() {

    routing {
        get("/department") {
            val department = DepartmentController(call).getDepartment()
            call.respond(department)
        }
    }

    routing {
        get("/rooms") {
            RoomController(call).getRoom()
        }
    }


    routing {
        get("/faculties") {
            val department = FacultyController(call).getFaculty()
            call.respond(department)
        }
    }

    routing {
        get("/groups") {
        GroupController(call).getGroup()
        }
    }

    routing {
        get("/students") {
            val groupId = call.parameters["group_id"]?.toLongOrNull()
            val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
            val students = StudentController(call).getStudent(groupId, token)
            call.respond(students)
        }
    }

    routing {
        post("/students/list") {
            val request = call.receive<FetchStudentRequestList>()
            val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
            val students = StudentController(call).getStudentList(request.studentIDList, token)
            call.respond(students)
        }
    }
}

