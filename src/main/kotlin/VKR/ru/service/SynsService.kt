package VKR.ru.routing

import VKR.ru.features.Faculty.models.FetchFacultyRequestFaculty
import VKR.ru.features.department.DepartmentController
import VKR.ru.features.department.FacultyController
import VKR.ru.features.department.LessonController
import VKR.ru.features.department.ParticipantController
import VKR.ru.features.department.RoomController
import VKR.ru.features.groups.GroupController
import VKR.ru.features.groups.models.FetchGroupRequestGroups
import VKR.ru.features.groups.models.FetchListDepartRequest
import VKR.ru.features.groups.models.FetchRoomRequestRooms
import VKR.ru.features.groups.models.NewLessonDTO
import VKR.ru.features.participant.models.UpdatedParticipantDTO
import VKR.ru.features.student.StudentController
import VKR.ru.features.student.models.FetchStudentRequestStudents
import VKR.ru.utils.TokenCheck
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureSyncService() {
    routing {
        post("/department/list") {
            val request = call.receive<FetchListDepartRequest>()
            val department = DepartmentController(call).getListDepartment(request.departmentIDList)
            call.respond(department)
        }
    }

    routing {
        post("/rooms/list") {
            val request = call.receive<FetchRoomRequestRooms>()
            val rooms = RoomController(call).getListRoom(request.roomIDList)
            call.respond(rooms)
        }
    }

    routing {
        post("/faculties/list") {
            val request = call.receive<FetchFacultyRequestFaculty>()
            val rooms = FacultyController(call).getListFaculty(request.facultyIDList)
            call.respond(rooms)
        }
    }

    routing {
        post("/groups/list") {
            val request = call.receive<FetchGroupRequestGroups>()
            val groups = GroupController(call).getListGroup(request.groupIDList)
            call.respond(groups)
        }
    }

    routing {
        post("/students/list/models") {
            val request = call.receive<FetchStudentRequestStudents>()
            val token = call.request.headers[VKR.ru.utils.Headers.X_ACCESS_TOKEN]
            if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
                val student = StudentController(call).getListStudent(request.studentIDList)
                call.respond(student)
            }
        }
    }

    routing {
        post("/lessons/new") {
            val data = call.receive<List<NewLessonDTO>>()
            LessonController(call).createLesson(data)
        }
    }

    routing {
        post("/participant/update") {
            val participants = call.receive<List<UpdatedParticipantDTO>>()
            ParticipantController(call).updateParticipant(participants)
        }
    }

}
