package de.rlang

import de.rlang.contact.contactModule
import de.rlang.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    configureSecurity(SecurityConfiguration.fromEnvironment(environment))
    contactModule()
}
