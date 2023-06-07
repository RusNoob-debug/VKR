package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val nameRoom: String,
    val idDepartment: Long
)

@Serializable
data class CreateRoomResponse(
    val id: Long,
    val name: String
)


@Serializable
data class ListDepartmentRequest(
    val id: Long,
    val name: String
)

@Serializable
data class ListRoomsRequest(
    val id: Long,
    val name: String
)

@Serializable
data class RoomWithDepartmentDTO(
    val rooms: ListRoomsRequest,
    val department: ListDepartmentRequest,
)