package VKR.ru.features.GroupOnLesson

import VKR.ru.database.groupsonlessons.GroupsOnLesson
import VKR.ru.database.groupsonlessons.mapToCreateGroupsOnLessonResponse
import VKR.ru.database.groupsonlessons.mapToGroupsOnLessonsDTO
import VKR.ru.features.GroupOnLesson.models.CreateGroupOnLessonRequest
import VKR.ru.utils.TokenCheck
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class GroupOnLessonController(private val call: ApplicationCall) {

    suspend fun createGroupOnLesson() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateGroupOnLessonRequest>()
            val groupOnLesson = request.mapToGroupsOnLessonsDTO()
            GroupsOnLesson.insert(groupOnLesson)
            call.respond(groupOnLesson.mapToCreateGroupsOnLessonResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}