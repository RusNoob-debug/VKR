package VKR.ru.database.disciplinelists

import kotlinx.serialization.Serializable

@Serializable
class DisciplinelistDTO (
    val teacherId: Long,
    val disciplineId: Long,
)



