package com.sportenth.data.database.entity

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Types
import java.time.LocalDateTime
import kotlin.io.use

object VerificationCodes : Table("verification_codes") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val code = varchar("code", 6)
    val createdAt = datetime("created_at")

}
