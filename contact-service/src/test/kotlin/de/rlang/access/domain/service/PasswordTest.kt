package de.rlang.access.domain.service

import de.rlang.access.domain.Password
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class PasswordTest {

    private val hash = "qNRyGt/3n2aJCE0TEiK4mw==\$lmKr3CaU/cIidFwKXjRMDrDNVd8n4DlzVxxi7Gnp2J8="

    @Test
    fun `verify returns true if the password is valid`() {
        val authenticator = Password()

        authenticator.verify("right password", hash) `should be` true
    }

    @Test
    fun `verify returns false if the password is invalid`() {
        val authenticator = Password()

        authenticator.verify("wrong password", hash) `should be` false
    }
}