package VKR.ru.features.groups.models

import VKR.ru.database.lessons.LessonType
import VKR.ru.database.lessons.Lessons
import VKR.ru.database.lessons.Status
import VKR.ru.database.lessons.VisitType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.javatime.time
import java.time.Duration
import java.time.LocalTime

@Serializable
data class CreateLessonRequest(
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

@Serializable
data class CreateLessonResponse(
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


@Serializable
data class NewLessonDTO(
    @SerialName("discipline_id") val disciplineID: Long,
    @SerialName("teacher_id") val teacherID: Long,
    @SerialName("room_id") val roomID: Long,
    val theme: String,
    val type: LessonType,
    @SerialName("start_time") val startTime: Long?,
    @SerialName("planned_duration") val plannedDuration: Double,
    @SerialName("actual_duration") val actualDuration: Double?,
    val participants: List<ListParticipantDTO>
)

@Serializable
data class ListParticipantDTO(
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

