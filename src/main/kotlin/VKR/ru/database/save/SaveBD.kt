package VKR.ru.database.save

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

val users = "users"
val department = "department"
val discipline = "discipline"
val disciplinelist = "disciplinelist"
val faculty = "faculty"
val groups = "groups"
val groupsonlesson = "groupsonlesson"
val ipaddress = "ipaddress"
val lessons = "lessons"
val participant = "participant"
val roles = "roles"
val room = "room"
val student = "student"
val tokens = "tokens"


val exportDirectory = "C:\\Log\\" // директория для сохранения файлов
val importDirectory = "C:\\Log\\" // директория для восстановления файлов

// Функция для экспорта всех таблиц в отдельные файлы
fun exportTablesToFile(tableNames: List<String>, directory: String) {
    for (tableName in tableNames) {
        val exportFilePath = "${directory}${tableName}.csv"
        exportDataToFile(tableName, exportFilePath)
    }
}

// Функция для импорта данных из файлов для каждой таблицы
fun importTablesFromFile(tableNames: List<String>, directory: String) {
    for (tableName in tableNames) {
        val importFilePath = "${directory}${tableName}.csv"
        importDataFromFile(tableName, importFilePath)
    }
}


// Пример использования
val tableNames = listOf(
    users,
    department,
    discipline,
    disciplinelist,
    faculty,
    groups,
    groupsonlesson,
    ipaddress,
    lessons,
    participant,
    roles,
    room,
    student,
    tokens
)



// Функция для добавления данных из таблицы базы данных в файл
fun exportDataToFile(tableName: String, filePath: String) {
    val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres", // URL подключения к базе данных
        "postgres", // Имя пользователя из pgAdmin
        "Afynjv764" // Пароль из pgAdmin
    )

    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM $tableName")

    val file = File(filePath)
    file.bufferedWriter().use { writer ->
        while (resultSet.next()) {
            val rowData = StringBuilder()
            for (i in 1..resultSet.metaData.columnCount) {
                rowData.append(resultSet.getString(i)).append(",")
            }
            writer.write(rowData.toString().removeSuffix(",") + "\n")
        }
    }

    resultSet.close()
    statement.close()
    connection.close()
}

// Функция для добавления данных из файла в таблицу базы данных
fun importDataFromFile(tableName: String, filePath: String) {
    val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres", // URL подключения к базе данных
        "postgres", // Имя пользователя из pgAdmin
        "Afynjv764" // Пароль из pgAdmin
    )

    val statement = connection.createStatement()

    val file = File(filePath)
    file.bufferedReader().use { reader ->
        var line: String? = reader.readLine()
        while (line != null) {
            val rowData = line.split(",")
            val query = "INSERT INTO $tableName VALUES (${rowData.joinToString(",") { "'$it'" }})"
            statement.executeUpdate(query)
            line = reader.readLine()
        }
    }

    statement.close()
    connection.close()
}



fun clearAllTables() {
    val connectionString = "jdbc:postgresql://localhost:5432/postgres"
    val driver = "org.postgresql.Driver"
    val user = "postgres"
    val password = "Afynjv764"

    // Список таблиц для очистки
    val tablesToClear = listOf(
        "users",
        "groups",
        "tokens",
        "student",
        "room",
        "roles",
        "participant",
        "lessons",
        "ipaddress",
        "groupsonlesson",
        "faculty",
        "discipline",
        "disciplinelist",
        "department"
    )

    try {
        // Создание подключения к базе данных
        DriverManager.getConnection(connectionString, user, password).use { connection ->
            // Отключение автокоммита, чтобы все операции были частью одной транзакции
            connection.autoCommit = false

            // Очистка каждой таблицы
            for (tableName in tablesToClear) {
                val deleteQuery = "DELETE FROM $tableName"

                connection.createStatement().use { statement ->
                    statement.executeUpdate(deleteQuery)
                }
            }

            // Подтверждение транзакции
            connection.commit()
        }
    } catch (e: Exception) {
        println("Error clearing tables: ${e.message}")
    }
}

