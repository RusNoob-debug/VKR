package VKR.ru.database.department

import VKR.ru.features.groups.models.CreateDepartmentRequest
import VKR.ru.features.groups.models.CreateDepartmentResponse
import VKR.ru.utils.GenUniqueIdDepartment
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentDTO (
    val idDepartment: Long,
    val nameDepartment: String,
)

fun CreateDepartmentRequest.mapToDepartmentDTO(): DepartmentDTO =
    DepartmentDTO(
        idDepartment = GenUniqueIdDepartment.generateRandomIdBuild(),
        nameDepartment = nameDepartment,
    )

fun DepartmentDTO.mapToCreateDepartmentResponse(): CreateDepartmentResponse =
    CreateDepartmentResponse(
        id = idDepartment,
        name = nameDepartment,
    )


