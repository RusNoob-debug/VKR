package VKR.ru.database.disciplines


import VKR.ru.features.groups.models.CreateDisciplineRequest
import VKR.ru.features.groups.models.CreateDisciplineResponse
import VKR.ru.utils.GenUniqueIdDiscipline
import kotlinx.serialization.Serializable

@Serializable
data class DisciplineDTO (
    val idDiscipline: Long,
    val nameDiscipline: String,
)

fun CreateDisciplineRequest.mapToDisciplineDTO(): DisciplineDTO =
    DisciplineDTO(
        idDiscipline = GenUniqueIdDiscipline.generateRandomIdDiscipline(),
        nameDiscipline = nameDiscipline,
    )

fun DisciplineDTO.mapToCreateDisciplineResponse(): CreateDisciplineResponse  =
    CreateDisciplineResponse(
        id = idDiscipline,
        name = nameDiscipline,
    )

