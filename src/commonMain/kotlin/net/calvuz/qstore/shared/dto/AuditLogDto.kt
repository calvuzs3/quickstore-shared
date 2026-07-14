package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuditLogEntryDto(
    val id: String,
    val userId: String,
    val userEmail: String,
    val actionKey: String,
    val entityTypeKey: String?,
    val entityId: String?,
    val detail: String?,
    val deviceInfo: String?,
    val createdAt: Long
)

/**
 * Paginazione all'indietro nel tempo: [entries] ordinate DESC per createdAt,
 * [nextBefore] è il createdAt dell'ultima riga di questa pagina — da passare come
 * `before` alla chiamata successiva. Null se questa pagina ha meno righe di quante
 * richieste (non ci sono altre pagine più vecchie).
 */
@Serializable
data class AuditLogPageResponse(
    val entries: List<AuditLogEntryDto>,
    val nextBefore: Long?
)
