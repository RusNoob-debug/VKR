package VKR.ru.features.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchDepartmentRequest(
    val searchQuery: String
)

@Serializable
data class FetchListDepartRequest(
    val departmentIDList: List<Long>
)