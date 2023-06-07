package VKR.ru.database.groups

import kotlinx.serialization.Serializable
import VKR.ru.features.groups.models.CreateGroupRequest
import VKR.ru.features.groups.models.CreateGroupResponse
import VKR.ru.utils.GenUniqueIdGroup

@Serializable
data class GroupDTO (
    val idGroup: Long,
    val nameGroup: String,
    val course: Byte,
    val faculty_id: Long,
)

fun CreateGroupRequest.mapToGroupDTO(): GroupDTO =
    GroupDTO(
        idGroup = GenUniqueIdGroup.generateRandomIdGroup(),
        nameGroup = nameGroup,
        course = course,
        faculty_id = faculty_id,
    )

fun GroupDTO.mapToCreateGroupResponse(): CreateGroupResponse =
    CreateGroupResponse(
        idGroup = idGroup,
        nameGroup = nameGroup,
        course = course,
    )

