package VKR.ru.features.department

import VKR.ru.database.disciplines.Discipline
import VKR.ru.database.disciplines.mapToCreateDisciplineResponse
import VKR.ru.database.disciplines.mapToDisciplineDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import VKR.ru.features.groups.models.CreateDisciplineRequest
import VKR.ru.features.groups.models.FetchDisciplineRequest
import VKR.ru.utils.TokenCheck

class DisciplineController(private val call: ApplicationCall) {

    suspend fun performDisciplineSearch() {
        val request = call.receive<FetchDisciplineRequest>()
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]

        if (TokenCheck.isTokenAdmin(token.orEmpty()) || TokenCheck.isTokenTeacher(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(Discipline.fetchAll())
            } else {
                call.respond(Discipline.fetchAll().filter { it.nameDiscipline.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    suspend fun createDiscipline() {
        val token = call.request.headers[VKR.ru.utils.Headers.X_ADMIN_TOKEN]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateDisciplineRequest>()
            val dis = request.mapToDisciplineDTO()
            Discipline.insert(dis)
            call.respond(dis.mapToCreateDisciplineResponse())
        } else {
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}