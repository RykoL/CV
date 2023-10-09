package de.rlang.access.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.rlang.plugins.SecurityConfiguration
import java.text.SimpleDateFormat
import java.util.*

data class AccessToken(
    val issuedFor: String,
    val expiresAt: Date,
    val token: String
) {
    companion object {
        fun create(visitor: String, expiresAt: String, securityConfiguration: SecurityConfiguration): AccessToken {
            val expirationDate = SimpleDateFormat("yyyy-MM-dd").parse(expiresAt)
            val jwt = JWT.create()
                .withAudience(securityConfiguration.audience)
                .withIssuer(securityConfiguration.issuer)
                .withSubject(visitor)
                .withClaim("role", Role.VISITOR.toString())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(securityConfiguration.secret))

            return AccessToken(visitor, expirationDate, jwt)
        }
    }
}