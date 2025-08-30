package com.gaglileo.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import com.gaglileo.models.UserSession

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 36000 // 1 hour
        }
    }
}
