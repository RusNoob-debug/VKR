package VKR.ru.features.participant.models

import VKR.ru.database.participants.ParticipantDTO
import VKR.ru.utils.GenUniqueIdLesson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpdatedParticipantDTO(
    @SerialName("lesson_id") val lessonID: Long,
    @SerialName("student_id") val studentID: Long,
    @SerialName("total_visit_duration") val totalVisitDuration: Double,
    @SerialName("is_marked") val isMarked: Boolean,
    @SerialName("theory_assessment") val theoryAssessment: Byte?,
    @SerialName("practice_assessment") val practiceAssessment: Byte?,
    @SerialName("activity_assessment") val activityAssessment: Byte?,
    @SerialName("prudence_assessment") val prudenceAssessment: Byte?,
    @SerialName("creativity_assessment") val creativityAssessment: Byte?,
    @SerialName("preparation_assessment") val preparationAssessment: Byte?,
    val note: String?
)


fun UpdatedParticipantDTO.toParticipantDTO(): ParticipantDTO {
    return ParticipantDTO(
        lesson_id = lessonID,
        student_id = studentID,
        total_visit_time = totalVisitDuration,
        is_marked = isMarked,
        theory_assessment = theoryAssessment,
        practice_assessment = practiceAssessment,
        activity_assessment = activityAssessment,
        prudence_assessment = prudenceAssessment,
        creativity_assessment = creativityAssessment,
        preparation_assessment = preparationAssessment,
        note = note
    )
}

@Serializable
data class UpdateParticipantResponse(
    val lesson_id: Long,
    val student_id: Long,
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

fun createUpdateParticipantResponse(lessonId: Long, updatedParticipant: ParticipantDTO): UpdateParticipantResponse {
    return UpdateParticipantResponse(
        lesson_id = lessonId,
        student_id = updatedParticipant.student_id,
        total_visit_time = updatedParticipant.total_visit_time,
        is_marked = updatedParticipant.is_marked,
        theory_assessment = updatedParticipant.theory_assessment,
        practice_assessment = updatedParticipant.practice_assessment,
        activity_assessment = updatedParticipant.activity_assessment,
        prudence_assessment = updatedParticipant.prudence_assessment,
        creativity_assessment = updatedParticipant.creativity_assessment,
        preparation_assessment = updatedParticipant.preparation_assessment,
        note = updatedParticipant.note
    )
}

