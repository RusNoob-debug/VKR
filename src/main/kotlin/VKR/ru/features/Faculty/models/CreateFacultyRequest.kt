package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable


@Serializable
data class CreateFacultyRequest(
    val nameFaculty: String
)

@Serializable
data class CreateFacultyResponse(
    val id: Long,
    val name: String
)

@Serializable
data class DeleteFacultyRequest(
    val idFaculty: Long,
)
