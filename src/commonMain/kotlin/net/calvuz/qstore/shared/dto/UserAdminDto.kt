package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

// Creazione account dal pannello admin (POST /users) — non un endpoint di
// registrazione self-service (deciso: vedi quickstore-server CLAUDE.md sezione 9),
// solo un ADMIN già autenticato può crearne uno, poi lo aggiunge alla propria org
// via POST /memberships (vedi MembershipDto.kt).

@Serializable
data class CreateUserRequest(
    val email: String,
    val password: String,
    val displayName: String? = null
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val displayName: String?
)
