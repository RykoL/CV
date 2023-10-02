package de.rlang.contact

import de.rlang.contact.application.getContactDetails
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.contactModule() {
    routing {
        getContactDetails()
    }
}