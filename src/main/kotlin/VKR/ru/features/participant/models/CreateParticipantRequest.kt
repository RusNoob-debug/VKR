package VKR.ru.features.groups.models

import VKR.ru.database.groups.GroupDTO
import VKR.ru.database.lessons.LessonType
import VKR.ru.database.lessons.Status
import VKR.ru.database.lessons.VisitType
import VKR.ru.database.participants.Participant
import VKR.ru.database.participants.ParticipantDTO
import VKR.ru.utils.GenUniqueIdLesson
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Duration

@Serializable
data class CreateParticipantRequest(
    val student_id: Long,
    val is_active: Boolean = false,
    val total_visit_time: Double,
    val is_marked: Boolean = false,
    val theory_assessment: Byte? = null,
    val practice_assessment: Byte? = null,
    val activity_assessment: Byte? = null,
    val prudence_assessment: Byte? = null,
    val creativity_assessment: Byte? = null,
    val preparation_assessment: Byte? = null,
    val note: String? = null,
)


@Serializable
data class CreateParticipantResponse(
    val lesson_id: Long,
    val student_id: Long,
    val is_active: Boolean = false,
    val total_visit_time: Double,
    val is_marked: Boolean = false,
    val theory_assessment: Byte? = null,
    val practice_assessment: Byte? = null,
    val activity_assessment: Byte?= null,
    val prudence_assessment: Byte? = null,
    val creativity_assessment: Byte? = null,
    val preparation_assessment: Byte? = null,
    val note: String? = null,
)

fun CreateParticipantRequest.mapToParticipantDTO(): ParticipantDTO =
    ParticipantDTO(
        lesson_id = GenUniqueIdLesson.generateRandomIdLesson(),
        student_id = student_id,
        total_visit_time = total_visit_time,
        is_marked = is_marked,
        theory_assessment = theory_assessment,
        practice_assessment = practice_assessment,
        activity_assessment = activity_assessment,
        prudence_assessment = prudence_assessment,
        creativity_assessment = creativity_assessment,
        preparation_assessment = preparation_assessment,
        note = note
    )


fun ParticipantDTO.mapToCreateParticipantResponse(): CreateParticipantResponse =
    CreateParticipantResponse(
        lesson_id = lesson_id,
        student_id = student_id,
        total_visit_time = total_visit_time,
        is_marked = is_marked,
        theory_assessment = theory_assessment,
        practice_assessment = practice_assessment,
        activity_assessment = activity_assessment,
        prudence_assessment = prudence_assessment,
        creativity_assessment = creativity_assessment,
        preparation_assessment = preparation_assessment,
        note = note
    )






/*fun StatisticsDTO.toStatistics(): Statistics = Statistics(
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)*/


