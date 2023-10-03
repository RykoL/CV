package de.rlang.contact

import de.rlang.contact.application.getContactDetails
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.contactModule() {
    routing {
        getContactDetails()

        get("/health") {
            call.respond(HttpStatusCode.OK)
        }
    }
}