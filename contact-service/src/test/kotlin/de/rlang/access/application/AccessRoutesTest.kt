package de.rlang.access.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.rlang.access.domain.Password
import de.rlang.contact.domain.models.Address
import de.rlang.contact.domain.models.Contact
import de.rlang.contact.domain.models.ContactDetails
import de.rlang.contact.domain.models.PersonalData
import de.rlang.main
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import java.util.*

class AccessRoutesTest {

    private var adminToken: String = JWT.create()
        .withAudience("test-audience")
        .withIssuer("test-issuer")
        .withClaim("role", "ADMIN")
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256("some-secret"))

    private val visitorToken: String = JWT.create()
        .withAudience("test-audience")
        .withIssuer("test-issuer")
        .withClaim("role", "VISITOR")
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256("some-secret"))
    @Test
    fun `returns a 403 if authenticated user is not an admin`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }
            val response = client.get("access/grant") {
                header("Authorization", "Bearer $visitorToken")
            }

            assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun `returns the grant access page if authenticated user is an admin`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            main()
        }
        val response = client.get("access/grant") {
            header("Authorization", "Bearer $adminToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Nested
    inner class Login {

        @Test
        fun `should return unprotected login route`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }
            val response = client.get("login")

            val expected = """
<html>
<body>
    <h1>Login</h1>
    <form method="post">
        <label for="username">
            Username
            <input type="text" name="username" id="username">
        </label>
        <label for="password">
            Password
            <input type="password" name="password" id="password">
        </label>
        <button type="submit">Login</button>
    </form>
</body>
</html>
            """.trimIndent()
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(expected, response.bodyAsText())
        }

        @Test
        fun `should return 404 on unsuccessful login attempt`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }
            val response = client.post("login") {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("username" to "test-user", "password" to "some-password").formUrlEncode())
            }

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

        @Test
        fun `should return 200 on successful login attempt`() = testApplication {
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                main()
            }
            val response = client.post("login") {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("username" to "some-username", "password" to "right password").formUrlEncode())
            }

            assertEquals(HttpStatusCode.Found, response.status)
        }

    }
}