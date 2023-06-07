package VKR.ru.features.GroupOnLesson.models

import kotlinx.serialization.Serializable


@Serializable
data class CreateGroupOnLessonRequest(
    val lesson_id: Long,
    val group_id: Long
)

@Serializable
data class CreateGroupOnLessonResponse(
    val lesson_id: Long,
    val group_id: Long
)

@Serializable
data class DeleteGroupOnLessonRequest(
    val lesson_id: Long,
    val group_id: Long
)