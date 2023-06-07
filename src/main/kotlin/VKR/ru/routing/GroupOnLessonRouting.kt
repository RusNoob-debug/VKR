package VKR.ru.routing

import VKR.ru.features.GroupOnLesson.GroupOnLessonController
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureFacultyRouting() {

    routing {
        post("/GroupOnLesson/create") {
            GroupOnLessonController(call).createGroupOnLesson()
        }
    }
}