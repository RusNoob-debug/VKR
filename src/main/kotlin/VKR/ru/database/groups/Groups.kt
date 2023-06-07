package VKR.ru.database.groups

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Groups : Table() {
    private val idGroup = Groups.long("idGroup")
    private val nameGroup = Groups.varchar(name = "nameGroup", length = 100)
    private val course = Groups.byte(name = "course")
    private val faculty_id = Groups.long(name = "faculty_id")


    fun insert(groupDTO: GroupDTO) {
        transaction {
            Groups.insert {
                it[idGroup] = groupDTO.idGroup
                it[nameGroup] = groupDTO.nameGroup
                it[course] = groupDTO.course
                it[faculty_id] = groupDTO.faculty_id
            }
        }
    }

    fun fetchAll(): List<GroupDTO> {
        return try {
            transaction {
                Groups.selectAll().toList()
                    .map {
                        GroupDTO(
                            idGroup = it[idGroup],
                            nameGroup = it[nameGroup],
                            course = it[course],
                            faculty_id = it[faculty_id],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchByName(groupName: String): GroupDTO? {
        return try {
            transaction {
                Groups.select { Groups.nameGroup eq groupName }
                    .mapNotNull {
                        GroupDTO(
                            idGroup = it[idGroup],
                            nameGroup = it[nameGroup],
                            course = it[course],
                            faculty_id = it[faculty_id],
                        )
                    }
                    .singleOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun hasMatchingIdGroup(idGroup: Long): Boolean {
        return try {
            transaction {
                Groups.select {
                    Groups.idGroup eq idGroup
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchById(idGroup: Long): GroupDTO? {
        return try {
            transaction {
                Groups.select { Groups.idGroup eq idGroup }
                    .mapNotNull {
                        GroupDTO(
                            idGroup = it[Groups.idGroup],
                            nameGroup = it[Groups.nameGroup],
                            course = it[Groups.course],
                            faculty_id = it[Groups.faculty_id],
                        )
                    }
                    .singleOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }
}