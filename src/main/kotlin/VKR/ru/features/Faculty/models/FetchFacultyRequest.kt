package VKR.ru.features.Faculty.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchFacultyRequest(
    val searchQuery: String
)

@Serializable
data class FetchFacultyRequestFaculty(
    val facultyIDList: List<Long>
)