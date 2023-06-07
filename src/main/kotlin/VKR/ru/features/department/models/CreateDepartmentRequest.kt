package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateDepartmentRequest(
    val nameDepartment: String,
)

@Serializable
data class CreateDepartmentResponse(
    val id: Long,
    val name: String,
)

@Serializable
data class UpdateDepartmentRequest(
    val idDepartment: Long,
    val newNameDepartment: String
)



@Serializable
data class DeleteDepartmentRequest(
    val idDepartment: Long,
)
