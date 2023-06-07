package VKR.ru.features.GroupOnLesson.models


import kotlinx.serialization.Serializable

@Serializable
data class FetchGroupOnLessonResponse(
    val Faculty: List<GroupOnLessonResponse>
)

@Serializable
data class GroupOnLessonResponse(
    val idFaculty: Long,
    val nameFaculty: String
)
