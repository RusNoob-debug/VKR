package VKR.ru.database.participants


import VKR.ru.database.groups.Groups
import VKR.ru.features.groups.models.ListParticipantDTO
import VKR.ru.features.groups.models.NewLessonDTO
import VKR.ru.utils.GenUniqueIdLesson
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


object Participant : Table() {
    private val lesson_id = long("lesson_id")
    private val student_id = long("student_id")
    private val total_visit_time = double("total_visit_time").nullable()
    private val is_marked = bool("is_marked")
    private val theory_assessment = byte("theory_assessment").nullable()
    private val practice_assessment = byte("practice_assessment").nullable()
    private val activity_assessment = byte("activity_assessment").nullable()
    private val prudence_assessment = byte("prudence_assessment").nullable()
    private val creativity_assessment = byte("creativity_assessment").nullable()
    private val preparation_assessment = byte("preparation_assessment").nullable()
    private val note = varchar("note", 100).nullable()

    fun insert(participantDTO: ParticipantDTO) {
        transaction {
            Participant.insert {
                it[lesson_id] = participantDTO.lesson_id
                it[student_id] = participantDTO.student_id
                it[total_visit_time] = participantDTO.total_visit_time
                it[is_marked] = participantDTO.is_marked
                it[theory_assessment] = participantDTO.theory_assessment
                it[practice_assessment] = participantDTO.practice_assessment
                it[activity_assessment] = participantDTO.activity_assessment
                it[prudence_assessment] = participantDTO.prudence_assessment
                it[creativity_assessment] = participantDTO.creativity_assessment
                it[preparation_assessment] = participantDTO.preparation_assessment
                it[note] = participantDTO.note
            }
        }
    }

    fun hasMatchingIdLesson(idLesson: Long): Boolean {
        return try {
            transaction {
                Participant.select {
                    Participant.lesson_id eq idLesson
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchAll(): List<ParticipantDTO> {
        return try {
            transaction {
                Participant.selectAll().toList()
                    .map {
                        ParticipantDTO(
                            lesson_id = it[Participant.lesson_id],
                            student_id = it[Participant.student_id],
                            total_visit_time = it[Participant.total_visit_time] ?: 0.0,
                            is_marked = it[Participant.is_marked],
                            theory_assessment = it[Participant.theory_assessment],
                            practice_assessment = it[Participant.practice_assessment],
                            activity_assessment = it[Participant.activity_assessment],
                            prudence_assessment = it[Participant.prudence_assessment],
                            creativity_assessment = it[Participant.creativity_assessment],
                            preparation_assessment = it[Participant.preparation_assessment],
                            note = it[Participant.note]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun updateByLessonId(lessonId: Long, updatedParticipant: ParticipantDTO) {
        transaction {
            Participant.update({ lesson_id eq lessonId }) {
                it[student_id] = updatedParticipant.student_id
                it[total_visit_time] = updatedParticipant.total_visit_time
                it[is_marked] = updatedParticipant.is_marked
                it[theory_assessment] = updatedParticipant.theory_assessment
                it[practice_assessment] = updatedParticipant.practice_assessment
                it[activity_assessment] = updatedParticipant.activity_assessment
                it[prudence_assessment] = updatedParticipant.prudence_assessment
                it[creativity_assessment] = updatedParticipant.creativity_assessment
                it[preparation_assessment] = updatedParticipant.preparation_assessment
                it[note] = updatedParticipant.note
            }
        }
    }

    fun insertParticipants(participants: List<ListParticipantDTO>, idLess: Long) {
        transaction {
            participants.forEach { participant ->
                Participant.insert {
                    it[lesson_id] = idLess
                    it[student_id] = participant.studentID
                    it[total_visit_time] = participant.totalVisitDuration
                    it[is_marked] = participant.isMarked
                    it[theory_assessment] = participant.theoryAssessment
                    it[practice_assessment] = participant.practiceAssessment
                    it[activity_assessment] = participant.activityAssessment
                    it[prudence_assessment] = participant.prudenceAssessment
                    it[creativity_assessment] = participant.creativityAssessment
                    it[preparation_assessment] = participant.preparationAssessment
                    it[note] = participant.note
                }
            }
        }
    }

}
