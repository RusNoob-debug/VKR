package VKR.ru.database.groupsonlessons

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object GroupsOnLesson : Table() {
    private val lesson_id = long("lesson_id")
    private val group_id = long("group_id")

    fun insert(groupsOnLessonDTO: GroupsOnLessonDTO) {
        transaction {
            GroupsOnLesson.insert {
                it[lesson_id] = groupsOnLessonDTO.lesson_id
                it[group_id] = groupsOnLessonDTO.group_id
            }
        }
    }

    fun fetchAll(): List<GroupsOnLessonDTO> {
        return try {
            transaction {
                GroupsOnLesson.selectAll().toList()
                    .map {
                        GroupsOnLessonDTO(
                            lesson_id = it[GroupsOnLesson.lesson_id],
                            group_id = it[GroupsOnLesson.group_id],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun insertAll(variable: Long, list: List<Long>) {
        transaction {
            list.forEach { element ->
                val groupsOnLessonDTO = GroupsOnLessonDTO(
                    lesson_id = variable,
                    group_id = element
                )
                insert(groupsOnLessonDTO)
            }
        }
    }

    fun isLessonIdInGroupIds(lesson: Long, groups: List<Long>): Boolean {
        val groupIds = fetchAll().map { it.group_id }
        return groupIds.containsAll(groups) && groupIds.contains(lesson)
    }

}