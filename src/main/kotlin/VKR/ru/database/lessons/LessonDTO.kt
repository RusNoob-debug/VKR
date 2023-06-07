package VKR.ru.database.lessons

import VKR.ru.features.groups.models.CreateLessonRequest
import VKR.ru.features.groups.models.CreateLessonResponse
import VKR.ru.features.groups.models.NewLessonDTO
import VKR.ru.utils.GenUniqueIdFaculty
import kotlinx.serialization.Serializable

enum class LessonType {
    LECTURE,
    PRACTICE
}

enum class VisitType {
    INTRAMURAL,
    REMOTE,
    MIX
}

enum class Status {
    CREATED,
    STARTED,
    FINISHED
}

@Serializable
class LessonDTO(
    val idLesson: Long,
    val discipline_id: Long,
    val teacher_id: Long,
    val room_id: Long,
    val type: LessonType,
    val theme: String,
    val visit_type: VisitType,
    val start_time: Long,
    val planned_duration: Double,
    val actual_duration: Double,
    val state: Status = Status.CREATED,
)

fun NewLessonDTO.mapToParticipantDTO(idLes: Long): LessonDTO =
    LessonDTO(
        idLesson = idLes,
        discipline_id = disciplineID,
        teacher_id = teacherID,
        room_id = roomID,
        type = LessonType.LECTURE,
        theme = theme,
        visit_type = VisitType.INTRAMURAL,
        start_time = startTime ?: 0,
        planned_duration = plannedDuration,
        actual_duration = actualDuration ?: 0.0,
        state = Status.CREATED
    )

