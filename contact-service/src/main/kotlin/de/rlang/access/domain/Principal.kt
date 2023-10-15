package de.rlang.access.domain

import io.ktor.server.auth.jwt.*


fun JWTPrincipal.isAdmin(): Boolean = enumValueOf<Role>(getClaim("role", String::class) ?: "") == Role.ADMIN