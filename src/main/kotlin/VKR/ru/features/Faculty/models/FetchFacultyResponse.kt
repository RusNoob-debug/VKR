package VKR.ru.features.Faculty.models


import kotlinx.serialization.Serializable

@Serializable
data class FetchDisciplineResponse(
    val Faculty: List<FacultyResponse>
)

@Serializable
data class FacultyResponse(
    val idFaculty: Long,
    val nameFaculty: String
)

@Serializable
data class FacultyDopDTO (
    val id: Long,
    val name: String,
)
