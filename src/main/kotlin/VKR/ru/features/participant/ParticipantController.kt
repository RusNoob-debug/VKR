package VKR.ru.features.department

import VKR.ru.database.disciplinelists.Disciplinelist
import VKR.ru.database.groups.Groups
import VKR.ru.database.groupsonlessons.GroupsOnLesson.isLessonIdInGroupIds
import VKR.ru.database.lessons.Lessons
import VKR.ru.database.participants.Participant
import VKR.ru.database.participants.ParticipantDTO
import VKR.ru.database.participants.mapToCreateParticipantResponse
import VKR.ru.database.participants.mapToParticipantDTO
import VKR.ru.database.teachers.Student
import VKR.ru.database.tokens.Tokens
import VKR.ru.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import VKR.ru.features.groups.models.CreateParticipantRequest

import VKR.ru.features.participant.models.UpdatedParticipantDTO
import VKR.ru.features.participant.models.createUpdateParticipantResponse
import VKR.ru.features.participant.models.toParticipantDTO
import VKR.ru.utils.TokenCheck


class ParticipantController(private val call: ApplicationCall) {

    suspend fun createParticipant() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateParticipantRequest>()
            val par = request.mapToParticipantDTO()
            Participant.insert(par)
            call.respond(par.mapToCreateParticipantResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun statAllParticipant() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val participants = Participant.fetchAll()
            call.respond(participants)
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun updateParticipant(participants: List<UpdatedParticipantDTO>) {

        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            participants.forEach { participant ->
                val lessonId = participant.lessonID
                val updatedParticipant = participant.toParticipantDTO()
                val idDis = getTeacherDisciplineIdsByToken(token)
                val idDiscip = Lessons.getIdDiscipline(participant.lessonID)
                if (idDis.contains(idDiscip)) {
                    if (Participant.hasMatchingIdLesson(lessonId)) {
                        Participant.updateByLessonId(lessonId, updatedParticipant)
                        val response = createUpdateParticipantResponse(lessonId, updatedParticipant)
                        call.respond(response)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Participant not found")
                    }
                }else {
                    call.respond(HttpStatusCode.Forbidden,"Access denied")
                }
            }
        } else {
            call.respond(HttpStatusCode.Forbidden,"Access denied")
        }
    }


    fun getTeacherDisciplineIdsByToken(token: String?): List<Long> {
        val login = Tokens.returnLoginByToken(token.orEmpty())
        if (login != null) {
            val tokenData: String = Tokens.returnLoginByToken(token.orEmpty()) ?: ""
            if (tokenData != null && tokenData == login) {
                val user = Users.fetchByLogin(login)
                if (user != null) {
                    val teacher = Users.fetchById(user.idUser)
                    if (teacher != null) {
                        val disciplineList = Disciplinelist.fetchByTeacherId(teacher.idUser)
                        return disciplineList.mapNotNull { it.disciplineId }
                    }
                }
            }
        }
        return emptyList()
    }

}