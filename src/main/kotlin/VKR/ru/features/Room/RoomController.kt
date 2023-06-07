package VKR.ru.features.department


import VKR.ru.database.department.Department
import VKR.ru.database.rooms.Room
import VKR.ru.database.rooms.mapToCreateRoomResponse
import VKR.ru.database.rooms.mapToRoomDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import VKR.ru.features.groups.models.CreateRoomRequest
import VKR.ru.features.groups.models.CreateRoomResponse
import VKR.ru.features.groups.models.FetchRoomRequestStandart
import VKR.ru.features.groups.models.ListDepartmentRequest
import VKR.ru.features.groups.models.ListRoomsRequest
import VKR.ru.features.groups.models.RoomWithDepartmentDTO
import VKR.ru.utils.TokenCheck

class RoomController(private val call: ApplicationCall) {

    suspend fun performRoomSearch() {
        val request = call.receive<FetchRoomRequestStandart>()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(Room.fetchAll())
            } else {
                call.respond(Room.fetchAll().filter { it.nameRoom.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun createRoom() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateRoomRequest>()
            val room = request.mapToRoomDTO()
            Room.insert(room)
            call.respond(room.mapToCreateRoomResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }


    suspend fun getRoom() {
        val departmentId = call.parameters["department_id"]?.toLongOrNull()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val rooms = if (departmentId != null) {
                val allRooms = Room.fetchAll()
                allRooms.filter { room ->
                    room.idDepartment == departmentId
                }.map { room ->
                    CreateRoomResponse(room.idRoom, room.nameRoom)
                }
            } else {
                emptyList()
            }
            call.respond(HttpStatusCode.OK, rooms)
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    fun getListRoom(idRoomList: List<Long>): List<RoomWithDepartmentDTO> {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val roomsWithDepartments = mutableListOf<RoomWithDepartmentDTO>()

            for (idRoom in idRoomList) {
                val room = Room.fetchByIdRoom(idRoom)

                if (room != null) {
                    val department = Department.fetchByIdDepartment(room.idDepartment)
                    if (department != null) {
                        val departmentRequest = ListDepartmentRequest(department.idDepartment, department.nameDepartment)
                        val roomRequest = ListRoomsRequest(room.idRoom, room.nameRoom)
                        val roomWithDepartmentDTO = RoomWithDepartmentDTO(roomRequest, departmentRequest)
                        roomsWithDepartments.add(roomWithDepartmentDTO)
                    } else {
                        val roomRequest = ListRoomsRequest(room.idRoom, room.nameRoom)
                        val roomWithDepartmentDTO = RoomWithDepartmentDTO(roomRequest, ListDepartmentRequest(0, ""))
                        roomsWithDepartments.add(roomWithDepartmentDTO)
                    }
                }
            }

            return roomsWithDepartments
        } else {
            return emptyList()
        }
    }

}