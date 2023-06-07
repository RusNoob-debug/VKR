package VKR.ru.features.groups.models


import kotlinx.serialization.Serializable

@Serializable
data class FetchRoomRequest(
    val department_id: Long
)

data class FetchRoomRequestStandart(
    val searchQuery: String
)
@Serializable
data class FetchRoomRequestRooms(
    val roomIDList: List<Long>
)

