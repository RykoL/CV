package de.rlang.contact.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val email: String,
    val tel: String,
    val linkedin: String
)

