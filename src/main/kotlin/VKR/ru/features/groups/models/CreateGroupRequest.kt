package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    val nameGroup: String,
    val course: Byte,
    val faculty_id: Long,
)

@Serializable
data class CreateGroupResponse(
    val idGroup: Long,
    val nameGroup: String,
    val course: Byte,
)

@Serializable
data class DeleteGroupRequest(
    val idGroup: Long,
)

@Serializable
data class IdFacultyAndGroupResponse(
    val id: Long,
    val name : String,
    val course: Byte,
)

@Serializable
data class IdStudentResponse(
    val id: Long,
    val name : String,
    val email: String,
)


@Serializable
data class ListGroupRequest(
    val id: Long,
    val name: String
)

@Serializable
data class ListFacultyRequest(
    val id: Long,
    val name: String
)

@Serializable
data class GroupWithFacultyDTO(
    val group: ListGroupRequest,
    val faculty: ListFacultyRequest,
)

