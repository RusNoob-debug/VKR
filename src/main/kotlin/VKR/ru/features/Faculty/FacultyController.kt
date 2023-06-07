package VKR.ru.features.department

import VKR.ru.database.department.Department
import VKR.ru.database.facultys.Faculty
import VKR.ru.database.facultys.mapToCreateFacultyResponse
import VKR.ru.database.facultys.mapToFacultyDTO
import VKR.ru.features.Faculty.models.FacultyDopDTO
import VKR.ru.features.Faculty.models.FetchFacultyRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import VKR.ru.features.groups.models.CreateFacultyRequest
import VKR.ru.utils.TokenCheck

class FacultyController(private val call: ApplicationCall) {

    suspend fun createFaculty() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateFacultyRequest>()
            val dis = request.mapToFacultyDTO()
            Faculty.insert(dis)
            call.respond(dis.mapToCreateFacultyResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun performFacultySearch() {
        val request = call.receive<FetchFacultyRequest>()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(Faculty.fetchAll())
            } else {
                call.respond(Faculty.fetchAll().filter { it.nameFaculty.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun getFaculty() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            call.respond(Department.fetchAll())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    fun getListFaculty(idFacultyList: List<Long>): List<FacultyDopDTO> {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val facultyList = mutableListOf<FacultyDopDTO>()

            for (idFaculty in idFacultyList) {
                val faculty = Faculty.fetchByIdFaculty(idFaculty)

                if (faculty != null) {
                    val facultyDopDTO = FacultyDopDTO(faculty.idFaculty, faculty.nameFaculty)
                    facultyList.add(facultyDopDTO)
                }
            }

            return facultyList
        } else {
            // Return an empty list if the user doesn't have sufficient privileges
            return emptyList()
        }
    }
}