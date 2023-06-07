package VKR.ru.features.groups

import VKR.ru.features.department.RoomController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoomRouting() {

    routing {
        post("/room/create") {
            RoomController(call).createRoom()
        }
        post("/room/search/all") {
            RoomController(call).performRoomSearch()
        }
    }
}