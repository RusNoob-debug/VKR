package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateDisciplineRequest(
    val nameDiscipline: String
)

@Serializable
data class CreateDisciplineResponse(
    val id: Long,
    val name: String
)

@Serializable
data class DeleteDisciplineRequest(
    val idDiscipline: Long,
)
