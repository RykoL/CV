package de.rlang.contact.infrastructure

import de.rlang.contact.domain.ports.outbound.ContactDetailReaderPort
import java.io.File
import kotlin.io.path.Path

class ContactDetailsReader : ContactDetailReaderPort {

    private val contactDetailsFileName: String = "contactDetails.yml"

    override fun readContactDetails(): String {
        val file = this.javaClass.classLoader.getResource(contactDetailsFileName).file
        return File(file).readText()
    }
}