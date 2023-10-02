package de.rlang.contact.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PersonalData(
    val name: String,
    val birthday: String,
    val role: String
)