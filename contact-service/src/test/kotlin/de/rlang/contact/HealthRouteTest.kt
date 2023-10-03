package de.rlang

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
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class HealthRouteTest {

    @Test
    fun `returns 200 when service is up`() = testApplication {
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
        val response = jsonClient.get("/health")

        assertEquals(HttpStatusCode.OK, response.status)
    }

}