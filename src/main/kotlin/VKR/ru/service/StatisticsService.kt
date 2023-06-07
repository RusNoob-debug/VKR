package VKR.ru.service


import VKR.ru.features.department.ParticipantController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureStatisticsService() {

    routing {
        get("/stat/all") {
            ParticipantController(call).statAllParticipant()
        }
    }
}