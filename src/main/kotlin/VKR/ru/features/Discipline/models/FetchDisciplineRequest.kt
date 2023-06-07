package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchDisciplineRequest(
    val searchQuery: String
)
