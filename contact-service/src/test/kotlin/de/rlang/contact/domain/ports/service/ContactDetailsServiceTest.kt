package de.rlang.contact.domain.ports.service

import de.rlang.contact.domain.models.Address
import de.rlang.contact.domain.models.Contact
import de.rlang.contact.domain.models.ContactDetails
import de.rlang.contact.domain.models.PersonalData
import de.rlang.contact.domain.ports.outbound.ContactDetailReaderPort
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import kotlin.test.Test


class ContactDetailsServiceTest {

    @Test
    fun `successfully parses contact details`() {
        val reader = mockk<ContactDetailReaderPort>()
        val service = ContactDetailsService(reader)

        val yml = """
           personalData:
             name: John Doe
             role: Singer
             birthday: 13.11.1990 
           address:
             street: Foo strt.
             city: Berlin 
             zip: 12345
           contact:
               email: foo@bar.com
               linkedin: "https://linkedin.com/in/peter.panski"
               tel: 0123456789
        """.trimIndent()

        every { reader.readContactDetails() } returns yml

        val actual = service.getContactDetails()
        val expected = ContactDetails(
            PersonalData("John Doe", "13.11.1990", "Singer"),
            Address("Foo strt.", "Berlin", "12345"),
            Contact("foo@bar.com", "0123456789", "https://linkedin.com/in/peter.panski"),
        )

        actual shouldBeEqualTo expected
    }
}