package com.sportenth.data.database

import com.sportenth.ExposedUser
import com.sportenth.UserService
import com.sportenth.UserService.Users.email
import com.sportenth.data.database.entity.VerificationCodes
import com.sportenth.data.requests.CodeVerificationRequest
import com.sportenth.data.requests.EmailRequest
import com.sportenth.data.response.TokenResponse
import com.sportenth.data.services.smtp.sendVerificationCode
import com.sportenth.data.services.smtp.tokens.generateAccessToken
import com.sportenth.data.services.smtp.tokens.generateRefreshToken
import io.ktor.http.*
import io.ktor.http.set
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.h2.engine.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.collections.set

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    val userService = UserService(database)
    routing {
        // Create user
        post("/users") {
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }

        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }

        post("/request-code") {
            val request = call.receive<EmailRequest>()
            val code = (100000..999999).random().toString()

            transaction {
                VerificationCodes.upsert(
                    VerificationCodes.email, VerificationCodes.code,
                    body = {
                        it[VerificationCodes.email] = request.email
                        it[VerificationCodes.code] = code
                        it[VerificationCodes.createdAt] = LocalDateTime.now()
                    },
                    /*where = {
                        VerificationCodes.email eq request.email
                    }*/)
            }

            sendVerificationCode(request.email, code)
            call.respond(mapOf("message" to "Verification code sent"))
        }

        // 2. Проверка кода
        post("/verify-code") {
            val request = call.receive<CodeVerificationRequest>()
            val storedCode = transaction {
                VerificationCodes.selectAll().where { VerificationCodes.email eq request.email }
                    .singleOrNull()?.get(VerificationCodes.code)
            }

            if (storedCode == null || storedCode != request.code) {
                call.respond(mapOf("error" to "Invalid code"))
                return@post
            }

            val userId = transaction {
                val existing =
                    UserService.Users.selectAll().where { UserService.Users.email eq request.email }.singleOrNull()
                if (existing != null) async { existing[UserService.Users.id].toString() }
                else {
                    async { userService.create(ExposedUser(request.email, age = 18, email = request.email)).toString() }
                }
            }

            // Удаляем использованный код
            transaction { VerificationCodes.deleteWhere { VerificationCodes.email eq request.email } }

            val accessToken = generateAccessToken(userId.await())
            val refreshToken = generateRefreshToken()

            call.respond(TokenResponse(accessToken, refreshToken))
        }

    }
}
