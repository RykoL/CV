package de.rlang

import de.rlang.contact.contactModule
import de.rlang.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::main)
        .start(wait = true)
}

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    configureSecurity(SecurityConfiguration.fromEnvironment(environment))
    contactModule()
}
