package de.rlang.access.application

import de.rlang.access.domain.AccessToken
import de.rlang.access.domain.Password
import de.rlang.access.domain.Role
import de.rlang.access.domain.isAdmin
import de.rlang.plugins.SecurityConfiguration
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Route.access() {

    route("/login") {
        get {
            call.respond(MustacheContent("login.hbs", null))
        }

        post {
            val params = call.receiveParameters()
            val userName: String = params.getOrFail("username")

            val providedPassword: String = params.getOrFail("password")
            val secretUserName = application.environment.config.property("auth.username").getString()
            val secretPW = application.environment.config.property("auth.password").getString()
            val password = Password()

            if (!password.verify(providedPassword, secretPW) || userName != secretUserName ) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }

            val securityConfiguration = SecurityConfiguration.fromEnvironment(application.environment)
            val token = AccessToken.create(
                secretUserName,
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                securityConfiguration,
                Role.ADMIN
            )
            call.response.cookies.append("AUTH", token.token)
            call.respondRedirect("/access/grant")
        }
    }

    authenticate {
        route("/access/grant") {
            get {
                val principal = call.principal<JWTPrincipal>()
                if (principal?.isAdmin() == false) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                val nextWeek = LocalDate.now().plusWeeks(1)
                call.respond(MustacheContent("generateToken.hbs", mapOf("date" to nextWeek.toString())))
            }

            post {
                val principal = call.principal<JWTPrincipal>()

                if (principal?.isAdmin() == false) {
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
}