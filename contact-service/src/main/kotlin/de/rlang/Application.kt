package de.rlang

import com.github.mustachejava.DefaultMustacheFactory
import de.rlang.contact.contactModule
import de.rlang.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.mustache.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates/mustache")
    }
    install (ContentNegotiation) {
        json()
    }
    configureSecurity(SecurityConfiguration.fromEnvironment(environment))
    contactModule()
}
