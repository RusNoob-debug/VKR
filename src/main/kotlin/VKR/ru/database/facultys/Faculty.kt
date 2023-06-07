package VKR.ru.database.facultys

import VKR.ru.database.disciplines.DisciplineDTO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Faculty : Table() {
    private val idFaculty = long("idFaculty")
    private val nameFaculty = varchar("nameFaculty", 50)

    fun insert(facultyDTO: FacultyDTO) {
        transaction {
            Faculty.insert {
                it[idFaculty] = facultyDTO.idFaculty
                it[nameFaculty] = facultyDTO.nameFaculty
            }
        }
    }

    fun hasMatchingIdFaculty(idFaculty: Long): Boolean {
        return try {
            transaction {
                Faculty.select {
                    Faculty.idFaculty eq idFaculty
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchByName(facultyName: String): FacultyDTO? {
        return try {
            transaction {
                Faculty.select { Faculty.nameFaculty eq facultyName }
                    .mapNotNull {
                        FacultyDTO(
                            idFaculty = it[idFaculty],
                            nameFaculty = it[nameFaculty],
                        )
                    }
                    .singleOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAll(): List<FacultyDTO> {
        return try {
            transaction {
                Faculty.selectAll().toList()
                    .map {
                        FacultyDTO(
                            idFaculty = it[Faculty.idFaculty],
                            nameFaculty = it[Faculty.nameFaculty],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun fetchByIdFaculty(id: Long): FacultyDTO? {
        return try {
            transaction {
                Faculty.select {
                    Faculty.idFaculty eq id
                }.singleOrNull()?.let {
                    FacultyDTO(
                        idFaculty = it[Faculty.idFaculty],
                        nameFaculty = it[Faculty.nameFaculty]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
