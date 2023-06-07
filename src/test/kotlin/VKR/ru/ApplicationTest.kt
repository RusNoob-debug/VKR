package VKR.ru

import VKR.ru.features.groups.configureRoomRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*


import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.testing.*
import org.junit.Test

class ApplicationTest {
    @Test
    fun testRegisterNewTeacher() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "/register/teacher") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("X_ADMIN_TOKEN", "ba-eff118-8efd-4d11-ba-eff118-8efd-4d11")
                setBody(
                    """
                {
                    "login": "userR",
                    "email": "User@mail.ru",
                    "password": "user",
                    "name": "Григорий Григорьевич Старобузов",
                    "nameDisciplines": ["Programming", "Program engineering", "Engineering"]
                }
                """.trimIndent()
                )
            }.apply {
                // Проверка статуса ответа
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}


