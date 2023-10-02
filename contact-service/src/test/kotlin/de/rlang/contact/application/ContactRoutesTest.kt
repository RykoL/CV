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
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.testing.*
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class ContactRoutesTest {

    private lateinit var token: String

    @Before
    fun setup() {
        token = JWT.create()
            .withAudience("test-audience")
            .withIssuer("test-issuers")
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256("some-secret"))
    }


    @Test
    fun `returns contact details`() = testApplication {
        application {
            main()
        }
        val jsonClient = createClient {
            install(ClientContentNegotiation) {
                json()
            }
        }
        val response = jsonClient.get("/contact-details") {
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
        application {
            main()
        }
        val jsonClient = createClient {
            install(ClientContentNegotiation) {
                json()
            }
        }
        val response = jsonClient.get("/contact-details") {
            header("Accept", "application/json")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `returns 401 if token is not valid`() = testApplication {
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
        val response = jsonClient.get("/contact-details") {
            header("Accept", "application/json")
            header("Authorization", "Bearer $invalidToken")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}