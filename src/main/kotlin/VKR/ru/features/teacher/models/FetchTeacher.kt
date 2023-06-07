package VKR.ru.features.teacher.models


import VKR.ru.database.lessons.LessonType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LessonDTO(
    val id: Long,
    @SerialName("discipline") val disciplineDTO: DisciplineDTO,
    @SerialName("teacher") val teacherDTO: TeacherDTO,
    @SerialName("room") val roomModelDTO: RoomModelDTO,
    @SerialName("groups") val groupModelDTOList: List<GroupModelDTO>,
    val theme: String,
    val type: LessonType,
    @SerialName("start_time") val startTime: Long?,
    @SerialName("planned_duration") val plannedDuration: Double,
    @SerialName("actual_duration") val actualDuration: Double?
)

@Serializable
data class TeacherDTO(
    val id: Long,
    val name: String,
    val email: String
)

@Serializable
data class DisciplineDTO(
    val id: Long,
    val name: String
)

@Serializable
data class RoomModelDTO(
    @SerialName("room") val roomDTO: RoomDTO,
    @SerialName("building") val buildingDTO: BuildingDTO
)

@Serializable
data class GroupModelDTO(
    @SerialName("group") val groupDTO: GroupDTO,
    @SerialName("faculty") val facultyDTO: FacultyDTO
)

@Serializable
data class BuildingDTO(
    val id: Long,
    val name: String
)

@Serializable
data class RoomDTO(
    val id: Long,
    val name: String
)

@Serializable
data class GroupDTO(
    val id: Long,
    val name: String,
    val course: Byte
)

@Serializable
data class FacultyDTO(
    val id: Long,
    val name: String
)