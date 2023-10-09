package de.rlang.access.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.rlang.access.domain.AccessToken
import de.rlang.access.domain.Role
import de.rlang.plugins.SecurityConfiguration
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

fun Route.access() {
    route("/access/grant") {
        get {
            val principal = call.principal<JWTPrincipal>()
            if (principal?.getClaim("role", String::class) != Role.ADMIN.toString()) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }
            val nextWeek = LocalDate.now().plusWeeks(1)
            call.respond(MustacheContent("generateToken.hbs", mapOf("date" to nextWeek.toString())))
        }

        post {
            val principal = call.principal<JWTPrincipal>()

            if (principal?.getClaim("role", String::class) != Role.ADMIN.toString()) {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }

            val params = call.receiveParameters()
            val visitorName = params["visitor"].toString()
            val expirationDate = params["expirationDate"].toString()
            val securityConfiguration = SecurityConfiguration.fromEnvironment(this.application.environment)

            val accessToken = AccessToken.create(visitorName, expirationDate, securityConfiguration)
            call.respond(
                MustacheContent(
                    "accessGranted.hbs", mapOf(
                        "token" to accessToken.token
                    )
                )
            )
        }
    }
}