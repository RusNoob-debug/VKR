package VKR.ru

import VKR.ru.database.save.exportDirectory
import VKR.ru.database.save.importTablesFromFile
import VKR.ru.database.save.tableNames
import VKR.ru.features.groups.configureDepartmentRouting
import VKR.ru.features.groups.configureDisciplineRouting
import VKR.ru.features.groups.configureFacultyRouting
import VKR.ru.features.login.configureLoginRouting
import VKR.ru.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import VKR.ru.plugins.*
import org.jetbrains.exposed.sql.Database
import VKR.ru.features.groups.configureGroupRouting
import VKR.ru.features.groups.configureRoomRouting
import VKR.ru.features.login.TokenCleanupTask
import VKR.ru.routing.configureParticipantRouting
import VKR.ru.routing.configureSyncService
import VKR.ru.routing.configureUniversityService
import VKR.ru.service.configureStatisticsService
import VKR.ru.service.configureUserService
import java.util.Timer
import java.util.TimerTask


fun main() {

    // Установка соединения с базой данных
    Database.connect(
        "jdbc:postgresql://localhost:5432/postgres",  // URL подключения к базе данных
        driver = "org.postgresql.Driver", // Драйвер PostgreSQL
        user = "postgres", // Имя пользователя из pgAdmin
        password = "Afynjv764" // Пароль из pgAdmin
    )

    val tokenCleanupTimer = Timer()
    tokenCleanupTimer.schedule(TokenCleanupTask(), 0L, 1800_000L)

    // Расписание для вызова функции записи каждые 24 часа
    tokenCleanupTimer.schedule(object : TimerTask() {
        override fun run() {
            importTablesFromFile(tableNames, exportDirectory)
        }
    }, 0L, 24 * 60 * 60 * 1000L)

    /*
    exportTablesToFile(tableNames, exportDirectory)
    clearAllTables()
    importTablesFromFile(tableNames, exportDirectory)
*/

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDisciplineRouting()
    configureStatisticsService()
    configureUserService()
    configureSyncService()
    configureParticipantRouting()
    configureRoomRouting()
    configureFacultyRouting()
    configureDepartmentRouting()
    configureRegisterRouting()
    configureGroupRouting()
    configureSerialization()
    configureLoginRouting()
    configureUniversityService()
}

