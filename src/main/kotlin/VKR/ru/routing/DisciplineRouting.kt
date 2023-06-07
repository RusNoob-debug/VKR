package VKR.ru.features.groups

import VKR.ru.features.department.DisciplineController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDisciplineRouting() {

    routing {
        post("/discipline/create") {
            DisciplineController(call).createDiscipline()
        }
        post("/discipline/search/all") {
            DisciplineController(call).performDisciplineSearch()
        }
    }
}