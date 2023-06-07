package VKR.ru.database.teachers

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object Teacher : Table() {
    private val idTeacher = long("idTeacher")
    private val email = varchar("email", 50)
    private val name = varchar("name", 50)

    fun insert(teacherDTO:TeacherDTO) {
        transaction {
            Teacher.insert {
                it[idTeacher] = teacherDTO.idTeacher
                it[email] = teacherDTO.email
                it[name] = teacherDTO.name
            }
        }
    }
}