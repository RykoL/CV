package de.rlang.contact.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String,
    val city: String,
    val zip: String
) {
    override fun toString(): String {
       return "$street, $zip $city"
    }
}