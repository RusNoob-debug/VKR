package VKR.ru.features.groups


import VKR.ru.features.department.FacultyController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureFacultyRouting() {

    routing {
        post("/faculty/create") {
            FacultyController(call).createFaculty()
        }
        post("/faculty/search/all") {
            FacultyController(call).performFacultySearch()
        }

    }
}