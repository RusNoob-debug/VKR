package VKR.ru.database.tokens


class TokenDTO (
    val idToken: Long,
    val login: String,
    val token: String,
    val expiration: Long
        )