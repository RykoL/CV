package de.rlang.contact.application.response

import de.rlang.contact.domain.models.Address
import de.rlang.contact.domain.models.ContactDetails

data class ContactDetailsPresentation(
    val address: Address,
    val email: String,
    val tel: String,
    val birthday: String
) {
    companion object {
        fun fromContactDetails(contactDetails: ContactDetails) =
            ContactDetailsPresentation(
                contactDetails.address,
                contactDetails.contact.email,
                contactDetails.contact.tel,
                contactDetails.personalData.birthday,
            )
    }
}