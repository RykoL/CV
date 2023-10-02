package de.rlang.contact.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ContactDetails(
    val personalData: PersonalData,
    val address: Address,
    val contact: Contact
)