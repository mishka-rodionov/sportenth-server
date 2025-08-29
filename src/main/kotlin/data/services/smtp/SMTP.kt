package com.sportenth.data.services.smtp

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

fun sendVerificationCode(email: String, code: String) {
    val username = "your_email@gmail.com"
    val password = "your_app_password"

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })

    val message = MimeMessage(session).apply {
        setFrom(InternetAddress(username))
        setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
        subject = "Your verification code"
        setText("Your verification code is: $code")
    }

    Transport.send(message)
    println("📧 Sent code $code to $email")
}
