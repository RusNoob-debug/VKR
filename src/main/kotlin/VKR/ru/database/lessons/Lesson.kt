package VKR.ru.database.lessons

import VKR.ru.features.groups.models.NewLessonDTO
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*

object Lessons : Table() {
    private val idLesson = long("idLesson")
    private val disciplineId = long("disciplineId")
    private val teacherId = long("teacherId")
    private val roomId = long("roomId")
    private val type = varchar("type", 50)
    private val theme = varchar("theme", 50)
    private val visitType = varchar("visitType", 50)
    private val startTime = long("startTime")
    private val plannedDuration = double("plannedDuration")
    private val actualDuration = double("actualDuration")
    private val state = varchar("state", 50)

    fun insert(lessonDTO: LessonDTO) {
        transaction {
            Lessons.insert {
                it[idLesson] = lessonDTO.idLesson
                it[disciplineId] = lessonDTO.discipline_id
                it[teacherId] = lessonDTO.teacher_id
                it[roomId] = lessonDTO.room_id
                it[type] = lessonDTO.type.name
                it[theme] = lessonDTO.theme
                it[visitType] = lessonDTO.visit_type.name
                it[startTime] = lessonDTO.start_time
                it[plannedDuration] = lessonDTO.planned_duration
                it[actualDuration] = lessonDTO.actual_duration
                it[state] = lessonDTO.state.name
            }
        }
    }

    fun hasMatchingIdLesson(idLesson: Long): Boolean {
        return try {
            transaction {
                Lessons.select {
                    Lessons.idLesson eq idLesson
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun getIdDiscipline(lessonId: Long): Long? {
        return transaction {
            Lessons.select {
                Lessons.idLesson eq lessonId
            }.singleOrNull()?.get(Lessons.disciplineId)
        }
    }
}