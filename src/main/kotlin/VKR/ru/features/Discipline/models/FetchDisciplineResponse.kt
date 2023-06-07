package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchDisciplineResponse(
    val Discipline: List<DisciplineResponse>
)

@Serializable
data class DisciplineResponse(
    val idDiscipline: Long,
    val nameDiscipline: String
)
