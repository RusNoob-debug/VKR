package VKR.ru.database.rooms

import VKR.ru.features.groups.models.CreateRoomRequest
import VKR.ru.features.groups.models.CreateRoomResponse
import VKR.ru.utils.GenUniqueIdRoom
import kotlinx.serialization.Serializable

@Serializable
data class RoomDTO (
    val idRoom: Long,
    val nameRoom: String,
    val idDepartment: Long,
)

fun CreateRoomRequest.mapToRoomDTO(): RoomDTO =
    RoomDTO(
        idRoom = GenUniqueIdRoom.generateRandomIdRoom(),
        nameRoom = nameRoom,
        idDepartment = idDepartment
    )

fun RoomDTO.mapToCreateRoomResponse(): CreateRoomResponse =
    CreateRoomResponse(
        id = idRoom,
        name = nameRoom,
    )