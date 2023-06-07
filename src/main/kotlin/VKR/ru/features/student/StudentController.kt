package VKR.ru.features.student

import VKR.ru.database.disciplinelists.Disciplinelist
import VKR.ru.database.disciplines.Discipline
import VKR.ru.database.facultys.Faculty
import VKR.ru.database.groups.Groups
import VKR.ru.database.teachers.Student
import VKR.ru.database.tokens.Tokens
import VKR.ru.database.users.Users
import VKR.ru.database.users.mapToIdResponseDTO
import VKR.ru.features.groups.models.IdStudentResponse
import VKR.ru.features.student.models.GroupWithFacultiesDTO
import VKR.ru.features.student.models.ListFacultiesRequest
import VKR.ru.features.student.models.ListGroupsRequest
import VKR.ru.features.student.models.StudentRequestGroupAndFacultyDTO
import VKR.ru.features.student.models.StudentResponse
import VKR.ru.features.student.models.TeacherDisciplinesResponse
import VKR.ru.features.student.models.TeacherRequestDTO
import VKR.ru.features.student.models.TeacherResponse
import io.ktor.server.application.*
import VKR.ru.utils.TokenCheck


class StudentController(private val call: ApplicationCall) {

    fun getStudent(groupId: Long?, token: String?): List<IdStudentResponse> {
        return if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            val students = if (groupId != null) {
                Users.fetchAll()
                    .filter { user ->
                        val student = Student.fetchById(user.idUser)
                        student?.idGroup == groupId && user.idUser == student.idStudent
                    }
                    .mapNotNull { user ->
                        val group = Groups.fetchById(groupId)
                        group?.let { user.mapToIdResponseDTO() }
                    }
            } else {
                emptyList()
            }
            students
        } else {
            emptyList()
        }
    }

    suspend fun getStudentList(groupIDList: List<Long>, token: String?): Map<Long, List<IdStudentResponse>> {
        val studentMap: MutableMap<Long, List<IdStudentResponse>> = mutableMapOf()

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            groupIDList.forEach { groupId ->
                val students = Users.fetchAll()
                    .filter { user ->
                        val student = Student.fetchById(user.idUser)
                        student?.idGroup == groupId && user.idUser == student.idStudent
                    }
                    .mapNotNull { user ->
                        val group = Groups.fetchById(groupId)
                        group?.let { user.mapToIdResponseDTO() }
                    }

                studentMap[groupId] = students
            }
        }
        return studentMap
    }

    fun getStudentResponse(studentIDList: List<Long>): List<IdStudentResponse> {
        val studentList: MutableList<IdStudentResponse> = mutableListOf()
            studentIDList.forEach { studentId ->
                val user = Users.fetchById(studentId)
                val student = Student.fetchById(studentId)

                if (user != null && student != null && user.idUser == student.idStudent) {
                    val group = Groups.fetchById(student.idGroup)
                    val idStudentResponse = IdStudentResponse(studentId, user.name, user.email)

                    studentList.add(idStudentResponse)
                }
            }
        return studentList
    }



    fun getListStudent(idStudentList: List<Long>): List<StudentRequestGroupAndFacultyDTO> {

            val studentRequestGroupAndFacultyList = mutableListOf<StudentRequestGroupAndFacultyDTO>()

            val studentList = getStudentResponse(idStudentList)

            for (student in studentList) {
                val user = Users.fetchById(student.id)
                val studentData = Student.fetchById(student.id)

                if (user != null && studentData != null && user.idUser == studentData.idStudent) {
                    val group = Groups.fetchById(studentData.idGroup)

                    if (group != null) {
                        val faculty = Faculty.fetchByIdFaculty(group.faculty_id)
                        val groupRequest = ListGroupsRequest(group.idGroup, group.nameGroup)
                        val facultyRequest = if (faculty != null) ListFacultiesRequest(faculty.idFaculty, faculty.nameFaculty)
                        else ListFacultiesRequest(0, "")

                        val groupWithFacultyDTO = GroupWithFacultiesDTO(groupRequest, facultyRequest)
                        val studentRequest = StudentResponse(student.id, student.name, student.email)

                        val studentRequestGroupAndFacultyDTO = StudentRequestGroupAndFacultyDTO(studentRequest, groupWithFacultyDTO)
                        studentRequestGroupAndFacultyList.add(studentRequestGroupAndFacultyDTO)
                    }
                }
            }
            return studentRequestGroupAndFacultyList
        }


    fun getListStudentForToken(token: String?): List<StudentRequestGroupAndFacultyDTO> {
        val login = Tokens.returnLoginByToken(token.orEmpty())
        if (login != null) {
            val user = Users.fetchByLogin(login)
            if (user != null) {
                val idStudentList = listOf(user.idUser)
                println(getListStudent(idStudentList))
                return getListStudent(idStudentList)
            }
        }
        return emptyList()
    }

    fun getTeacherDisciplinesByToken(token: String?): List<TeacherRequestDTO> {
        val login = Tokens.returnLoginByToken(token.orEmpty())
        if (login != null) {
            val tokenData: String = Tokens.returnLoginByToken(token.orEmpty()) ?: ""
            if (tokenData != null && tokenData == login) {
                val user = Users.fetchByLogin(login)
                if (user != null) {
                    val teacher = Users.fetchById(user.idUser)
                    if (teacher != null) {
                        val disciplineList = Disciplinelist.fetchByTeacherId(teacher.idUser)
                        val disciplines = disciplineList.mapNotNull { disciplineId ->
                            Discipline.fetchById(disciplineId.disciplineId)?.let { discipline ->
                                TeacherDisciplinesResponse(discipline.idDiscipline, discipline.nameDiscipline)
                            }
                        }
                        val teacherResponse = TeacherResponse(teacher.idUser, user.name ?: "", user.email ?: "")
                        val teacherRequestDTO = TeacherRequestDTO(teacherResponse, disciplines)
                        return listOf(teacherRequestDTO)
                    }
                }
            }
        }
        return emptyList()
    }


    fun getListTeacherForToken(token: String?): List<TeacherRequestDTO> {
        val login = Tokens.returnLoginByToken(token.orEmpty())
        if (login != null) {
            val userId = Users.returnIdUserByLogin(login)
            if (userId != null) {
                val teacher = Users.fetchById(userId)
                return teacher?.let { teacher ->
                    val disciplineList = Disciplinelist.fetchByTeacherId(teacher.idUser)
                    val disciplines = disciplineList.mapNotNull { disciplineId ->
                        Discipline.fetchById(disciplineId.disciplineId)?.let { discipline ->
                            TeacherDisciplinesResponse(discipline.idDiscipline, discipline.nameDiscipline)
                        }
                    }
                    val user = Users.fetchById(teacher.idUser)
                    val teacherResponse = TeacherResponse(teacher.idUser, user?.name ?: "", user?.email ?: "")
                    listOf(TeacherRequestDTO(teacherResponse, disciplines))
                } ?: emptyList()
            }
        }
        return emptyList()
    }

}




