package VKR.ru.routing

import VKR.ru.features.department.ParticipantController
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureParticipantRouting() {

    routing {
        post("/participant/create") {
            ParticipantController(call).createParticipant()
        }
    }
}