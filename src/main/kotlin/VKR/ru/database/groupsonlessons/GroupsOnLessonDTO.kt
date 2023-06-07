package VKR.ru.database.groupsonlessons

import VKR.ru.database.facultys.FacultyDTO
import VKR.ru.features.GroupOnLesson.models.CreateGroupOnLessonRequest
import VKR.ru.features.GroupOnLesson.models.CreateGroupOnLessonResponse
import VKR.ru.features.groups.models.CreateFacultyRequest
import VKR.ru.features.groups.models.CreateFacultyResponse
import VKR.ru.utils.GenUniqueIdFaculty
import kotlinx.serialization.Serializable

@Serializable
class GroupsOnLessonDTO (
    val lesson_id: Long,
    val group_id: Long,
)

fun CreateGroupOnLessonRequest.mapToGroupsOnLessonsDTO(): GroupsOnLessonDTO =
    GroupsOnLessonDTO(
        lesson_id = lesson_id,
        group_id = group_id,
    )

fun GroupsOnLessonDTO.mapToCreateGroupsOnLessonResponse(): CreateGroupOnLessonResponse =
    CreateGroupOnLessonResponse(
        lesson_id = lesson_id,
        group_id = group_id,
    )