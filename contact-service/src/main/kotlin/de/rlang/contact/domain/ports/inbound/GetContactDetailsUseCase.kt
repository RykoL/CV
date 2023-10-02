package de.rlang.contact.domain.ports.inbound

import de.rlang.contact.domain.models.ContactDetails

interface GetContactDetailsUseCase {
    fun getContactDetails(): ContactDetails
}