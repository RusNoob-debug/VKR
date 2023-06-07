package VKR.ru.database.department

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Department : Table() {
    private val idDepartment = Department.long("idDepartment")
    private val nameDepartment = Department.varchar(name = "nameDepartment", length = 100)


    fun insert(buildingDTO: DepartmentDTO) {
        transaction {
            Department.insert {
                it[idDepartment] = buildingDTO.idDepartment
                it[nameDepartment] = buildingDTO.nameDepartment
            }
        }
    }

    fun hasMatchingIdDepartment(idDepartment: Long): Boolean {
        return try {
            transaction {
                Department.select {
                    Department.idDepartment eq idDepartment
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchAll(): List<DepartmentDTO> {
        return try {
            transaction {
                Department.selectAll().toList()
                    .map {
                        DepartmentDTO(
                            idDepartment = it[Department.idDepartment],
                            nameDepartment = it[Department.nameDepartment]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteByIdDepartment(id: Long) {
        transaction {
            Department.deleteWhere { Department.idDepartment eq id }
        }
    }

    fun updateByIdDepartment(id: Long, updatedDepartment: DepartmentDTO) {
        transaction {
            Department.update({ Department.idDepartment eq id }) {
                it[nameDepartment] = updatedDepartment.nameDepartment
            }
        }
    }

    fun fetchByIdDepartment(id: Long): DepartmentDTO? {
        return try {
            transaction {
                Department.select {
                    Department.idDepartment eq id
                }.singleOrNull()?.let {
                    DepartmentDTO(
                        idDepartment = it[Department.idDepartment],
                        nameDepartment = it[Department.nameDepartment]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
