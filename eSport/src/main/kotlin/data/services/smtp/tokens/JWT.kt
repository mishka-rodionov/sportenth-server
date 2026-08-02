package com.competra.data.services.smtp.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.competra.data.util.requireEnv
import java.util.Date
import java.util.UUID
import kotlin.time.Duration.Companion.hours

val jwtIssuer: String = System.getenv("JWT_ISSUER") ?: "ktor_server"
val jwtAudience: String = System.getenv("JWT_AUDIENCE") ?: "ktor_audience"
val jwtSecret: String = requireEnv("JWT_SECRET")

fun generateAccessToken(userId: String): String =
    JWT.create()
        .withIssuer(jwtIssuer)
        .withAudience(jwtAudience)
        .withClaim("userId", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + 1.hours.inWholeMilliseconds))
        .sign(Algorithm.HMAC256(jwtSecret))

fun generateRefreshToken(): String = UUID.randomUUID().toString()