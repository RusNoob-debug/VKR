package VKR.ru.utils


import java.util.UUID
import kotlin.random.Random

object GenUniqueToken {
        fun generateUniqueToken(): String {
        var token = UUID.randomUUID().toString()
        while (TokenCheck.isTokenExists(token)) {
            token = UUID.randomUUID().toString()
        }
        return token
    }
}

object GenUniqueIdToken {
    fun generateUniqueIdToken(): Long {
        var idToken= Random.nextLong(1, Long.MAX_VALUE)
        while (IdTokenCheck.generateUniqueIdToken(idToken)) {
            idToken = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idToken
    }
}

object GenUniqueIdUser {
    fun generateRandomIDUser(): Long {
        var idUser = Random.nextLong(1, Long.MAX_VALUE)
        while (IdUserCheck.generateUniqueIDUser(idUser)) {
            idUser = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idUser
    }
}

object GenUniqueIdGroup {
    fun generateRandomIdGroup(): Long {
        var idGroup = Random.nextLong(1, Long.MAX_VALUE)
        while (IdGroupCheck.generateUniqueIDUser(idGroup)) {
            idGroup = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idGroup
    }
}

object GenUniqueIdLesson {
    fun generateRandomIdLesson(): Long {
        var idLesson = Random.nextLong(1, Long.MAX_VALUE)
        while (IdGroupLesson.generateUniqueIDLesson(idLesson)) {
            idLesson = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idLesson
    }
}

object GenUniqueIdParticipant{
    fun generateRandomIdParticipant(): Long {
        var idParticipant = Random.nextLong(1, Long.MAX_VALUE)
        while (IdGroupParticipant.generateUniqueIDParticipant(idParticipant)) {
            idParticipant = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idParticipant
    }
}

object GenUniqueIdDepartment {
    fun generateRandomIdBuild(): Long {
        var idDeportment = Random.nextLong(1, Long.MAX_VALUE)
        while (IdBuildCheck.generateUniqueIdDepartment(idDeportment)) {
            idDeportment = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idDeportment
    }
}

object GenUniqueIdDiscipline {
    fun generateRandomIdDiscipline(): Long {
        var idDis = Random.nextLong(1, Long.MAX_VALUE)
        while (IdDisciplineCheck.generateUniqueIdDiscipline(idDis)) {
            idDis = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idDis
    }
}

object GenUniqueIdFaculty {
    fun generateRandomIdFaculty(): Long {
        var idFaculty = Random.nextLong(1, Long.MAX_VALUE)
        while (IdFacultyCheck.generateUniqueIdFaculty(idFaculty)) {
            idFaculty = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idFaculty
    }
}

object GenUniqueIdRoom {
    fun generateRandomIdRoom(): Long {
        var idRoom = Random.nextLong(1, Long.MAX_VALUE)
        while (IdRoomCheck.generateUniqueIdRoom(idRoom)) {
            idRoom = Random.nextLong(1, Long.MAX_VALUE)
        }
        return idRoom
    }
}




