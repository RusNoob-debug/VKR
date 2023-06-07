package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchRoomResponse(
    val Room: List<RoomResponse>
)

@Serializable
data class RoomResponse(
    val idRoom: Long,
    val nameRome: String
)
