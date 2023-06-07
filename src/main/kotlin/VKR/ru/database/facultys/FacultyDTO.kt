package VKR.ru.database.facultys


import VKR.ru.features.groups.models.CreateFacultyRequest
import VKR.ru.features.groups.models.CreateFacultyResponse
import VKR.ru.utils.GenUniqueIdFaculty
import kotlinx.serialization.Serializable

@Serializable
data class FacultyDTO (
    val idFaculty: Long,
    val nameFaculty: String,
)


fun CreateFacultyRequest.mapToFacultyDTO(): FacultyDTO =
    FacultyDTO(
        idFaculty = GenUniqueIdFaculty.generateRandomIdFaculty(),
        nameFaculty = nameFaculty,
    )

fun FacultyDTO.mapToCreateFacultyResponse(): CreateFacultyResponse =
    CreateFacultyResponse(
        id = idFaculty,
        name = nameFaculty,
    )

