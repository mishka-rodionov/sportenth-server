package com.sportenth.data.database.entity

import org.jetbrains.exposed.sql.Table

object VerificationCodes : Table("verification_codes") {
    val email = varchar("email", 255).uniqueIndex()
    val code = varchar("code", 6)
//    val createdAt = datetime("created_at")
}