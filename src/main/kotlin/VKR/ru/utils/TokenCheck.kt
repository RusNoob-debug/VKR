package VKR.ru.utils

import VKR.ru.database.department.Department
import VKR.ru.database.disciplines.Discipline
import VKR.ru.database.facultys.Faculty
import VKR.ru.database.groups.Groups
import VKR.ru.database.lessons.Lessons
import VKR.ru.database.participants.Participant
import VKR.ru.database.roles.Roles.hasRoleByIdRole
import VKR.ru.database.rooms.Room
import VKR.ru.database.tokens.Tokens
import VKR.ru.database.tokens.Tokens.fetchTokens
import VKR.ru.database.tokens.Tokens.returnLoginByToken
import VKR.ru.database.users.Users
import VKR.ru.database.users.Users.returnIdUserByLogin


internal object Headers {
    const val X_AUTHENTICATION = "X_Authentication"
    const val X_ACCESS_TOKEN = "X_Access_Token"
    const val X_ADMIN_TOKEN = "X_Admin_Token"
}

object TokenCheck {

    fun isTokenValid(token: String): Boolean = fetchTokens().isNotEmpty()

    fun isTokenAdmin(token: String): Boolean = token == "ba-eff118-8efd-4d11-ba-eff118-8efd-4d11"

    fun isTokenTeacher(token: String): Boolean {
        val login = returnLoginByToken(token)
        val idUser = login?.let { returnIdUserByLogin(it) }
        return if (idUser != null) {
            hasRoleByIdRole(idUser)
        } else {
            false
        }
    }


    fun isTokenExists(token: String): Boolean {
        return Tokens.hasMatchingToken(token)
    }
}

object IdTokenCheck {
    fun generateUniqueIdToken(IdToken: Long): Boolean {
        return Tokens.hasMatchingIdToken(IdToken)
    }
}

object IdUserCheck {
    fun generateUniqueIDUser(idUser: Long): Boolean {
        return Users.hasMatchingIdUser(idUser)
    }
}

object IdGroupCheck {
    fun generateUniqueIDUser(idGroup: Long): Boolean {
        return Groups.hasMatchingIdGroup(idGroup)
    }
}

object IdGroupLesson {
    fun generateUniqueIDLesson(idLesson: Long): Boolean {
        return Lessons.hasMatchingIdLesson(idLesson)
    }
}
object IdGroupParticipant {
    fun generateUniqueIDParticipant(idLesson: Long): Boolean {
        return Participant.hasMatchingIdLesson(idLesson)
    }
}


object IdBuildCheck {
    fun generateUniqueIdDepartment(idDepartment: Long): Boolean {
        return Department.hasMatchingIdDepartment(idDepartment)
    }
}

object IdDisciplineCheck {
    fun generateUniqueIdDiscipline(idDis: Long): Boolean {
        return Discipline.hasMatchingIdDiscipline(idDis)
    }
}

object IdFacultyCheck {
    fun generateUniqueIdFaculty(idFaculty: Long): Boolean {
        return Faculty.hasMatchingIdFaculty(idFaculty)
    }
}

object IdRoomCheck {
    fun generateUniqueIdRoom(idRoom: Long): Boolean {
        return Room.hasMatchingIdRoom(idRoom)
    }
}

object IdLessonsCheck {
    fun generateUniqueIdLesson(idLesson: Long): Boolean {
        return Lessons.hasMatchingIdLesson(idLesson)
    }
}
