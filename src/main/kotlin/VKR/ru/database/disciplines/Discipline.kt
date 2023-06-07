package VKR.ru.database.disciplines

import VKR.ru.database.facultys.FacultyDTO
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Discipline : Table() {

    private val idDiscipline = long("idDiscipline")
    private val nameDiscipline = varchar("nameDiscipline", 50)

    fun insert(disciplineDTO: DisciplineDTO) {
        transaction {
            Discipline.insert {
                it[idDiscipline] = disciplineDTO.idDiscipline
                it[nameDiscipline] = disciplineDTO.nameDiscipline
            }
        }
    }


    fun hasMatchingIdDiscipline(idDis: Long): Boolean {
        return try {
            transaction {
                Discipline.select {
                    Discipline.idDiscipline eq idDis
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchAll(): List<DisciplineDTO> {
        return try {
            transaction {
                Discipline.selectAll().toList()
                    .map {
                        DisciplineDTO(
                            idDiscipline = it[Discipline.idDiscipline],
                            nameDiscipline = it[Discipline.nameDiscipline],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchById(id: Long): DisciplineDTO? {
        return try {
            transaction {
                Discipline.select {
                    Discipline.idDiscipline eq id
                }.singleOrNull()?.let {
                    DisciplineDTO(
                        idDiscipline = it[Discipline.idDiscipline],
                        nameDiscipline = it[Discipline.nameDiscipline]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchNameById(id: Long): String? {
        return try {
            transaction {
                Discipline.select {
                    Discipline.idDiscipline eq id
                }.singleOrNull()?.let {
                    it[Discipline.nameDiscipline]
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}