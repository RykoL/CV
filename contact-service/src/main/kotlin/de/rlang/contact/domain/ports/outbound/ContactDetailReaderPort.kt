package de.rlang.contact.domain.ports.outbound

import de.rlang.contact.domain.models.ContactDetails

interface ContactDetailReaderPort {
    fun readContactDetails(): String
}