package de.rlang.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*

fun Application.configureSecurity(securityConfiguration: SecurityConfiguration) {
    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(securityConfiguration.secret))
                    .withAudience(securityConfiguration.audience)
                    .withIssuer(securityConfiguration.issuer)
                    .build()
            )
            authHeader {
                val cookie = it.request.cookies["AUTH"].let {cookieValue ->
                    if (cookieValue == null) {
                        return@let it.request.header("authorization") ?: ""
                    }

                    "Bearer $cookieValue"
                }
                try {
                    parseAuthorizationHeader(cookie)
                } catch (cause: IllegalArgumentException) {
                    cause.message
                    null
                }
            }
            validate { credential ->
                if (credential.payload.audience.contains(securityConfiguration.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
