package de.rlang.contact.application

import de.rlang.contact.application.response.ContactDetailsPresentation
import de.rlang.contact.domain.ports.inbound.GetContactDetailsUseCase
import de.rlang.contact.domain.ports.service.ContactDetailsService
import de.rlang.contact.infrastructure.ContactDetailsReader
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.mustache.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getContactDetails() {
    val contactDetailsService: GetContactDetailsUseCase = ContactDetailsService(ContactDetailsReader())
    authenticate {
        get("api/contact-details") {
            call.respond(
                contactDetailsService.getContactDetails()
            )
        }

        get("/contact-details") {
            val contactDetails = ContactDetailsPresentation.fromContactDetails(contactDetailsService.getContactDetails())
            call.respond(MustacheContent("contactDetails.hbs", mapOf("contactDetails" to contactDetails)))
        }
    }
}