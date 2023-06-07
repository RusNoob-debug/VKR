package VKR.ru.features.groups

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGroupRouting() {

    routing {
        post("/groups/create") {
            GroupController(call).createGroup()
        }
        post("/groups/search/all") {
            GroupController(call).performGroupSearch()
        }
    }
}