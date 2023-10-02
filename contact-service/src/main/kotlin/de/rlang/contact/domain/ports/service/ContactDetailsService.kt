package de.rlang.contact.domain.ports.service

import de.rlang.contact.domain.models.ContactDetails
import de.rlang.contact.domain.ports.inbound.GetContactDetailsUseCase
import de.rlang.contact.domain.ports.outbound.ContactDetailReaderPort
import net.mamoe.yamlkt.Yaml

class ContactDetailsService(val contactDetailsReader: ContactDetailReaderPort) : GetContactDetailsUseCase {

    override fun getContactDetails(): ContactDetails =
        Yaml.decodeFromString(ContactDetails.serializer(), contactDetailsReader.readContactDetails())
}