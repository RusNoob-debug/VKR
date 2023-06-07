package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchGroupRequest(
    val faculty_id: Long
)

data class FetchGroupRequestConst(
    val searchQuery: String
)

@Serializable
data class FetchGroupRequestGroups(
    val groupIDList: List<Long>
)