package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchGroupResponse(
    val group: List<GroupResponse>
)

@Serializable
data class GroupResponse(
    val idGroup: Long,
    val nameGroup: String,
    val course: Int,
    val faculty_id: Long,
)
