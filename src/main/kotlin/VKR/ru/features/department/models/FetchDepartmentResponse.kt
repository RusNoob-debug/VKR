package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentResponse(
    val idBuilding: Long,
    val nameBuilding: String,
)

@Serializable
data class DepartmentDopDTO (
    val id: Long,
    val name: String,
)

@Serializable
data class FetchDepartmentResponse(
    val Build: List<DepartmentResponse>
)
