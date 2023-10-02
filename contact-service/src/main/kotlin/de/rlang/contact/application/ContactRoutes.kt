package de.rlang.contact.application

import de.rlang.contact.domain.models.Address
import de.rlang.contact.domain.models.Contact
import de.rlang.contact.domain.models.ContactDetails
import de.rlang.contact.domain.models.PersonalData
import de.rlang.contact.domain.ports.inbound.GetContactDetailsUseCase
import de.rlang.contact.domain.ports.service.ContactDetailsService
import de.rlang.contact.infrastructure.ContactDetailsReader
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getContactDetails() {
    val contactDetailsService: GetContactDetailsUseCase = ContactDetailsService(ContactDetailsReader())
    authenticate {
        get("/contact-details") {
            call.respond(
                contactDetailsService.getContactDetails()
            )
        }

    }
}