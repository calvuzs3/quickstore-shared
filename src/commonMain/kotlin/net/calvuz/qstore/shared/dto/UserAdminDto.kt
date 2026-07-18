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

// Modifica utente (PUT /users/{id}) — email deliberatamente esclusa: è la chiave
// usata per il login e per l'invito via POST /memberships, resta congelata (stessa
// scelta già fatta in QReport per lo username). displayName è sempre inviato
// (anche vuoto, per poterlo cancellare); password è opzionale, null = non cambiarla.
@Serializable
data class UpdateUserRequest(
    val displayName: String?,
    val password: String? = null
)
