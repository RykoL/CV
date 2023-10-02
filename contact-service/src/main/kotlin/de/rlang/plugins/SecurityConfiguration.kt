package de.rlang.plugins

import io.ktor.server.application.*

data class SecurityConfiguration(
    val audience: String,
    val issuer: String,
    val secret: String
) {
    companion object {
        fun fromEnvironment(environment: ApplicationEnvironment): SecurityConfiguration =
            SecurityConfiguration(
                audience = environment.config.property("jwt.audience").getString(),
                issuer = environment.config.property("jwt.issuer").getString(),
                secret = environment.config.property("jwt.secret").getString()
            )
    }
}