package VKR.ru.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq



object Tokens: Table() {
    private val idToken = long("idToken")
    private val login = Tokens.varchar("login", 25)
    private val token = Tokens.varchar("token", 50)
    private val expiration = long("expiration")

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[idToken] = tokenDTO.idToken
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
                it[expiration] = tokenDTO.expiration
            }
        }
    }

    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.select {
                    expiration greaterEq System.currentTimeMillis() // выбираем только неистекшие токены
                }.toList().map {
                        TokenDTO(
                            idToken = it[Tokens.idToken],
                            token = it[Tokens.token],
                            login = it[Tokens.login],
                            expiration = it[expiration]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun returnLoginByToken(token: String): String? {
        return try {
            transaction {
                Tokens.select {
                    Tokens.token eq token
                }.singleOrNull()?.get(Tokens.login)
            }
        } catch (e: Exception) {
            null
        }
    }


    fun hasMatchingToken(token: String): Boolean {
        return try {
            transaction {
                Tokens.select {
                    Tokens.token eq token
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun hasMatchingIdToken(idToken: Long): Boolean {
        return try {
            transaction {
                Tokens.select {
                    Tokens.idToken eq idToken
                }.limit(1).count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun deleteExpiredTokens() {
        transaction {
            Tokens.deleteWhere { Tokens.expiration lessEq System.currentTimeMillis() }
        }
    }
}