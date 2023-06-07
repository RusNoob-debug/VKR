package VKR.ru.features.groups

import VKR.ru.database.facultys.Faculty
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import VKR.ru.database.groups.Groups
import VKR.ru.database.groups.mapToCreateGroupResponse
import VKR.ru.database.groups.mapToGroupDTO
import VKR.ru.features.groups.models.CreateGroupRequest
import VKR.ru.features.groups.models.FetchGroupRequestConst
import VKR.ru.features.groups.models.GroupWithFacultyDTO
import VKR.ru.features.groups.models.ListFacultyRequest
import VKR.ru.features.groups.models.ListGroupRequest
import VKR.ru.utils.TokenCheck

class GroupController(private val call: ApplicationCall) {

    suspend fun performGroupSearch() {
        val request = call.receive<FetchGroupRequestConst>()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(Groups.fetchAll())
            } else {
                call.respond(Groups.fetchAll().filter { it.nameGroup.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun createGroup() {

        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateGroupRequest>()
            val group = request.mapToGroupDTO()
            Groups.insert(group)
            call.respond(group.mapToCreateGroupResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun getGroup() {
        val facultyId = call.parameters["faculty_id"]?.toLongOrNull()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val groups = if (facultyId is Long) {
                Groups.fetchAll().filter { group ->
                    group.faculty_id == facultyId
                }.map { group ->
                    group.mapToCreateGroupResponse()
                }
            }  else {
                emptyList()
            }

            call.respond(groups)
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    fun getListGroup(idRoomList: List<Long>): List<GroupWithFacultyDTO> {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val groupWithFacultyList = mutableListOf<GroupWithFacultyDTO>()

            for (idRoom in idRoomList) {
                val group = Groups.fetchById(idRoom)

                if (group != null) {
                    val faculty = Faculty.fetchByIdFaculty(group.idGroup)
                    if (faculty != null) {
                        val groupRequest = ListGroupRequest(group.idGroup, group.nameGroup)
                        val facultyRequest = ListFacultyRequest(faculty.idFaculty, faculty.nameFaculty)
                        val groupWithFacultyDTO = GroupWithFacultyDTO(groupRequest, facultyRequest)
                        groupWithFacultyList.add(groupWithFacultyDTO)
                    } else {
                        val groupRequest = ListGroupRequest(group.idGroup, group.nameGroup)
                        val groupWithFacultyDTO = GroupWithFacultyDTO(groupRequest, ListFacultyRequest(0, ""))
                        groupWithFacultyList.add(groupWithFacultyDTO)
                    }
                }
            }

            return groupWithFacultyList
        } else {
            return emptyList()
        }
    }
}