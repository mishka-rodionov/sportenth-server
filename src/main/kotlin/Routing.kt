package com.sportenth

import com.codahale.metrics.*
import com.sportenth.UserService.Users.email
import com.sportenth.data.database.entity.VerificationCodes
import com.sportenth.data.requests.CodeVerificationRequest
import com.sportenth.data.requests.EmailRequest
import com.sportenth.data.response.TokenResponse
import com.sportenth.data.services.smtp.sendVerificationCode
import com.sportenth.data.services.smtp.tokens.generateAccessToken
import com.sportenth.data.services.smtp.tokens.generateRefreshToken
import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.*
import io.github.damir.denis.tudor.ktor.server.rabbitmq.rabbitMQ
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.metrics.dropwizard.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.*
import java.time.LocalDateTime

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
        get("/info") {
            call.respondText("Hello World!")
        }

    }
}
