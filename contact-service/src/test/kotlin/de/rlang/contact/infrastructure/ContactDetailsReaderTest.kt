package de.rlang.contact.infrastructure

import org.amshove.kluent.shouldBeEqualTo
import kotlin.test.Test

class ContactDetailsReaderTest {

    @Test
    fun `should read contact details from resources`() {
        val contactDetailsReader = ContactDetailsReader()

        val expected = """
           personalData:
             name: John Doe
             role: Singer
             birthday: 13.11.1990
           address:
             street: Foo strt. 13
             city: Berlin
             zip: 12345
           contact:
             email: foo@bar.com
             linkedin: "https://linkedin.com/in/peter.panski"
             tel: 0123456789
        """.trimIndent()

        val actual = contactDetailsReader.readContactDetails()

        actual shouldBeEqualTo  expected
    }

}