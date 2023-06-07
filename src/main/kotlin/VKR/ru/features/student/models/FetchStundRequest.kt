package VKR.ru.features.student.models


import kotlinx.serialization.Serializable

@Serializable
open class FetchStudentRequest(
    val group_id: Long
)

@Serializable
data class IdStudentResponse(
    val id: Long,
    val name: String,
    val email: String
)


@Serializable
data class FetchStudentRequestList(
    val studentIDList: List<Long>
)

@Serializable
data class FetchStudentRequestStudents(
    val studentIDList: List<Long>
)

@Serializable
data class ListGroupsRequest(
    val id: Long,
    val name: String
)

@Serializable
data class StudentResponse(
    val id: Long,
    val name: String,
    val email: String
)

@Serializable
data class ListFacultiesRequest(
    val id: Long,
    val name: String
)

@Serializable
data class GroupWithFacultiesDTO(
    val group: ListGroupsRequest,
    val faculty: ListFacultiesRequest,
)

@Serializable
data class StudentRequestGroupAndFacultyDTO(
    val student: StudentResponse,
    val group : GroupWithFacultiesDTO,
)

@Serializable
data class TeacherResponse(
    val id: Long,
    val name: String,
    val email: String
)

@Serializable
data class TeacherDisciplinesResponse(
    val id: Long,
    val name: String
)

@Serializable
data class TeacherRequestDTO(
    val teacher: TeacherResponse,
    val disciplines : List<TeacherDisciplinesResponse>,
)






