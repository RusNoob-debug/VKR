package VKR.ru.database.disciplinelists

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Disciplinelist : Table() {
    private val teacher_id = long("teacher_id")
    private val discipline_id = long("discipline_id")

    suspend fun insert(disciplinelistDTO: DisciplinelistDTO) {
        transaction {
            Disciplinelist.insert {
                it[teacher_id] = disciplinelistDTO.teacherId
                it[discipline_id] = disciplinelistDTO.disciplineId
            }
        }
    }

    fun fetchByTeacherId(teacherId: Long): List<DisciplinelistDTO> {
        return transaction {
            Disciplinelist
                .select { Disciplinelist.teacher_id eq teacherId }
                .map { rowToDisciplinelistDTO(it) }
        }
    }

    private fun rowToDisciplinelistDTO(row: ResultRow): DisciplinelistDTO {
        return DisciplinelistDTO(
            teacherId = row[Disciplinelist.teacher_id],
            disciplineId = row[Disciplinelist.discipline_id]
        )
    }


}