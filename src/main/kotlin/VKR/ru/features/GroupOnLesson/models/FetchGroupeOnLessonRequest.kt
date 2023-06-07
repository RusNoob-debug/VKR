package VKR.ru.features.GroupOnLesson.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchGroupOnLessonRequest(
    val searchQuery: String
)