package VKR.ru.database.teachers

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Student : Table() {
    private val idStudent = long("idStudent")
    private val idGroup = long("idGroup")

    fun insert(rolesDTO:StudentDTO) {
        transaction {
            Student.insert {
                it[idStudent] = rolesDTO.idStudent
                it[idGroup] = rolesDTO.idGroup
            }
        }
    }

    fun fetchAll(): List<StudentDTO> {
        return try {
            transaction {
                Student.selectAll().toList()
                    .map {
                        StudentDTO(
                            idStudent = it[Student.idStudent],
                            idGroup = it[Student.idGroup],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchById(idStudent: Long): StudentDTO? {
        return try {
            transaction {
                Student.select {
                    Student.idStudent eq idStudent
                }.mapNotNull {
                    StudentDTO(
                        idStudent = it[Student.idStudent],
                        idGroup = it[Student.idGroup],
                    )
                }.singleOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getIdGroupByIdStudents(idStudents: List<Long>): List<Long> {
        return try {
            transaction {
                Student.select {
                    Student.idStudent inList idStudents
                }.mapNotNull {
                    it[Student.idGroup]
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}