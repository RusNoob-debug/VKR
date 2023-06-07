package VKR.ru.features.department

import VKR.ru.database.department.Department
import VKR.ru.database.department.DepartmentDTO
import VKR.ru.database.department.mapToCreateDepartmentResponse
import VKR.ru.database.department.mapToDepartmentDTO
import VKR.ru.features.groups.models.CreateDepartmentRequest
import VKR.ru.features.groups.models.DepartmentDopDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import VKR.ru.features.groups.models.FetchDepartmentRequest
import VKR.ru.features.groups.models.UpdateDepartmentRequest
import VKR.ru.utils.TokenCheck

class DepartmentController(private val call: ApplicationCall) {

    suspend fun performDepatrmentSearch() {
        val request = call.receive<FetchDepartmentRequest>()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(Department.fetchAll())
            } else {
                call.respond(Department.fetchAll().filter { it.nameDepartment.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun createDepatrment() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateDepartmentRequest>()
            val build = request.mapToDepartmentDTO()
            Department.insert(build)
            call.respond(build.mapToCreateDepartmentResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun updateDepatrment() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val id = call.parameters["id"]?.toLongOrNull()
            val request = call.receive<UpdateDepartmentRequest>()

            if (id != null && Department.hasMatchingIdDepartment(id)) {
                val updatedDepartment = DepartmentDTO(id, request.newNameDepartment)
                Department.updateByIdDepartment(id, updatedDepartment)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun deleteBuild() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val id = call.parameters["id"]?.toLongOrNull()

            if (id != null && Department.hasMatchingIdDepartment(id)) {
                Department.deleteByIdDepartment(id)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun getDepartment() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            call.respond(Department.fetchAll())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    fun getListDepartment(idDepartmentList: List<Long>): List<DepartmentDopDTO> {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val departments = mutableListOf<DepartmentDopDTO>()

            for (idDepartment in idDepartmentList) {
                val department = Department.fetchByIdDepartment(idDepartment)

                if (department != null) {
                    val departmentDopDTO = DepartmentDopDTO(department.idDepartment, department.nameDepartment)
                    departments.add(departmentDopDTO)
                }
            }
            return departments
        } else {
            return emptyList()
        }
    }

}
