package de.rlang.access.domain

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64

class Password {

    private val memoryAsKB: Int = 19456
    private val iterations: Int = 2
    private val parallels: Int = 1

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        return salt
    }

    fun encode(rawPassword: String): String {
        val encoder = Base64.getEncoder()
        val salt = generateSalt()
        val parameters: Argon2Parameters = getArgon2Parameters(salt)

        val hash = ByteArray(32)
        val verifier = Argon2BytesGenerator()
        verifier.init(parameters)
        verifier.generateBytes(rawPassword.toByteArray(StandardCharsets.UTF_8), hash)
        return "${encoder.encodeToString(salt)}\$${encoder.encodeToString(hash)}"
    }

    private fun getArgon2Parameters(salt: ByteArray): Argon2Parameters =
        Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withMemoryAsKB(memoryAsKB)
            .withIterations(iterations)
            .withParallelism(parallels)
            .withSalt(salt)
            .build()

    private fun decodeHashedPassword(hashedPassword: String): Pair<ByteArray, ByteArray> {
        val decoder = Base64.getDecoder()
        println(hashedPassword)
        val (salt, hashPart) = hashedPassword.split("$")

        return Pair(decoder.decode(salt), decoder.decode(hashPart))
    }

    fun verify(passwordToBeTested: String, hashedPassword: String): Boolean {
        val (salt, decodedHash) = decodeHashedPassword(hashedPassword)

        val parameters: Argon2Parameters = getArgon2Parameters(salt)

        val hash = ByteArray(32)
        val verifier = Argon2BytesGenerator()
        verifier.init(parameters)
        verifier.generateBytes(passwordToBeTested.toByteArray(StandardCharsets.UTF_8), hash)
        return hash.contentEquals(decodedHash)
    }
}