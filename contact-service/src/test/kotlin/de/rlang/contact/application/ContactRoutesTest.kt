package de.rlang.contact.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.rlang.contact.domain.models.Address
import de.rlang.contact.domain.models.Contact
import de.rlang.contact.domain.models.ContactDetails
import de.rlang.contact.domain.models.PersonalData
import de.rlang.main
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.testing.*
import org.junit.jupiter.api.*
import java.util.*
import kotlin.test.assertEquals

class ContactRoutesTest {

    private var token: String = JWT.create()
        .withAudience("test-audience")
        .withIssuer("test-issuer")
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256("some-secret"))
    @Nested
    inner class Api {
        @Test
        fun `returns contact details`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }
            val jsonClient = createClient {
                install(ClientContentNegotiation) {
                    json()
                }
            }
            val response = jsonClient.get("api/contact-details") {
                header("Accept", "application/json")
                header("Authorization", "Bearer $token")
            }

            val expectedBody = ContactDetails(
                PersonalData("John Doe", "13.11.1990", "Singer"),
                Address("Foo strt.", "Berlin", "12345"),
                Contact("foo@bar.com", "0123456789", "https://linkedin.com/in/peter.panski"),
            )

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(expectedBody, response.body())
        }

        @Test
        fun `returns 401 if user is not authenticated`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }
            val jsonClient = createClient {
                install(ClientContentNegotiation) {
                    json()
                }
            }
            val response = jsonClient.get("/api/contact-details") {
                header("Accept", "application/json")
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }

        @Test
        fun `returns 401 if token is not valid`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }

            val invalidToken = JWT.create()
                .withAudience("test-audience")
                .withIssuer("test-issuers")
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256("wrong-secret"))

            val jsonClient = createClient {
                install(ClientContentNegotiation) {
                    json()
                }
            }
            val response = jsonClient.get("/api/contact-details") {
                header("Accept", "application/json")
                header("Authorization", "Bearer $invalidToken")
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }

    @Nested
    inner class HTML {

        @Test
        fun `returns the contact details as html fragment`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }

            val response = client.get("/contact-details") {
                header("Authorization", "Bearer $token")
            }

            val expectedBody = """
               <ul class="undecorated-list">
                   <li class="icon home-icon">Foo strt. 13, 12345 Berlin</li>
                   <li class="icon email-icon">foo@bar.com</li>
                   <li class="icon contact-icon">0123456789</li>
                   <li class="icon birthday-icon">13.11.1990</li>
               </ul>
            """.trimIndent()

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(expectedBody, response.body())
        }

        @Test
        fun `returns 401 if user is not authenticated`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }

            val response = client.get("/api/contact-details")

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }

        @Test
        fun `returns 401 if token is not valid`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }

            val invalidToken = JWT.create()
                .withAudience("test-audience")
                .withIssuer("test-issuers")
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256("wrong-secret"))


            val response = client.get("/api/contact-details") {
                header("Authorization", "Bearer $invalidToken")
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }
}