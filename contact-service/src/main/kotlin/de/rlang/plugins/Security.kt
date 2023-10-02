package de.rlang.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(securityConfiguration: SecurityConfiguration) {
    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(securityConfiguration.secret))
                    .withAudience(securityConfiguration.audience)
                    .withIssuer(securityConfiguration.secret)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(securityConfiguration.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
