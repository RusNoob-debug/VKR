package VKR.ru.features.department


import VKR.ru.database.disciplinelists.Disciplinelist
import VKR.ru.database.groupsonlessons.GroupsOnLesson
import VKR.ru.database.lessons.Lessons
import VKR.ru.database.lessons.mapToParticipantDTO
import VKR.ru.database.participants.Participant
import VKR.ru.database.teachers.Student
import VKR.ru.database.tokens.Tokens
import VKR.ru.database.users.Users
import VKR.ru.features.groups.models.NewLessonDTO
import VKR.ru.utils.GenUniqueIdLesson
import VKR.ru.utils.TokenCheck
import io.ktor.server.application.*
import io.ktor.server.response.respond


class LessonController(private val call: ApplicationCall) {

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


    suspend fun createLesson(data: List<NewLessonDTO>) {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]

        val responseList = mutableListOf<NewLessonDTO>()

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            data.forEach { lesson ->
                val idLes = GenUniqueIdLesson.generateRandomIdLesson()

                val idDis = getTeacherDisciplineIdsByToken(token)

                if (idDis.contains(lesson.disciplineID)) {
                    val lessonDTO = lesson.mapToParticipantDTO(idLes)
                    Lessons.insert(lessonDTO)
                    Participant.insertParticipants(lesson.participants, idLes)
                    val participantIds = lesson.participants.map { it.studentID }
                    val idGroups = Student.getIdGroupByIdStudents(participantIds)
                    GroupsOnLesson.insertAll(idLes, idGroups)
                    responseList.add(lesson)
                }
            }
        }
        call.respond(responseList)
    }



}