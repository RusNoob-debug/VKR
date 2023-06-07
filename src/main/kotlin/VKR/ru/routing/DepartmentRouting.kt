package VKR.ru.features.groups

import VKR.ru.features.department.DepartmentController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDepartmentRouting() {

    routing {
        post("/department/create") {
            DepartmentController(call).createDepatrment()
        }

        post("/department/search/all") {
            DepartmentController(call).performDepatrmentSearch()
        }

        put("/update/{id}") {
            DepartmentController(call).updateDepatrment()
        }

        delete("/department/{id}") {
            DepartmentController(call).deleteBuild()

        }
    }
}