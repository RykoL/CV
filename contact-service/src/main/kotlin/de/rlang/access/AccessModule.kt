package de.rlang.access

import de.rlang.access.application.access
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.accessModule() {
    routing {
        authenticate {
            access()
        }
    }
}