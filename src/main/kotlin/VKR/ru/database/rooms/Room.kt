package VKR.ru.database.rooms

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Room : Table() {
    private val idRoom = Room.long("idRoom")
    private val nameRoom = Room.varchar(name = "nameRoom", length = 100)
    private val idDepartment = Room.long("idDepartment")

    fun insert(roomDTO: RoomDTO) {
        transaction {
            Room.insert {
                it[idRoom] = roomDTO.idRoom
                it[nameRoom] = roomDTO.nameRoom
                it[idDepartment] = roomDTO.idDepartment
            }
        }
    }

    fun hasMatchingIdRoom(idRoom: Long): Boolean {
        return try {
            transaction {
                Room.select {
                    Room.idRoom eq idRoom
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchAll(): List<RoomDTO> {
        return try {
            transaction {
                Room.selectAll().toList()
                    .map {
                        RoomDTO(
                            idRoom = it[Room.idRoom],
                            nameRoom = it[Room.nameRoom],
                            idDepartment = it[Room.idDepartment],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchAllRoom(): List<RoomDTO> {
        return try {
            transaction {
                Room.slice(Room.idRoom, Room.nameRoom)
                    .selectAll()
                    .map {
                        RoomDTO(
                            idRoom = it[Room.idRoom],
                            nameRoom = it[Room.nameRoom],
                            idDepartment = it[Room.idDepartment],
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchByIdRoom(id: Long): RoomDTO? {
        return try {
            transaction {
                Room.select {
                    Room.idRoom eq id
                }.singleOrNull()?.let {
                    RoomDTO(
                        idRoom = it[Room.idRoom],
                        nameRoom = it[Room.nameRoom],
                        idDepartment = it[Room.idDepartment]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchRoomIdsByDepartmentId(idDepartment: Long?): List<Long?> {
        return try {
            transaction {
                if (idDepartment != null) {
                    Room.slice(Room.idRoom)
                        .select { Room.idDepartment eq idDepartment }
                        .map { it[Room.idRoom] }
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
