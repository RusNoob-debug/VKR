package VKR.ru.database.participants

import VKR.ru.features.groups.models.CreateParticipantRequest
import VKR.ru.features.groups.models.CreateParticipantResponse
import VKR.ru.utils.GenUniqueIdParticipant
import kotlinx.serialization.Serializable

@Serializable
class ParticipantDTO (
    val lesson_id: Long,
    val student_id: Long,
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

fun CreateParticipantRequest.mapToParticipantDTO(): ParticipantDTO =
    ParticipantDTO(
        lesson_id = GenUniqueIdParticipant.generateRandomIdParticipant(),
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
