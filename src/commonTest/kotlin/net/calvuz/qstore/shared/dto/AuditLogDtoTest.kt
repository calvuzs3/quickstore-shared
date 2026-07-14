package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuditLogDtoTest {

    private val json = Json

    @Test
    fun auditLogEntryDto_roundTrip_withNullableFields() {
        val dto = AuditLogEntryDto(
            id = "log-1", userId = "user-1", userEmail = "a@b.com",
            actionKey = "ARTICLE_CREATED", entityTypeKey = null, entityId = null,
            detail = null, deviceInfo = null, createdAt = 1000L
        )
        assertEquals(dto, json.decodeFromString<AuditLogEntryDto>(json.encodeToString(dto)))
    }

    @Test
    fun auditLogEntryDto_roundTrip_fullyPopulated() {
        val dto = AuditLogEntryDto(
            id = "log-1", userId = "user-1", userEmail = "a@b.com",
            actionKey = "ARTICLE_DELETED", entityTypeKey = "ARTICLE", entityId = "art-1",
            detail = "cancellato da UI", deviceInfo = "Pixel 8", createdAt = 1000L
        )
        assertEquals(dto, json.decodeFromString<AuditLogEntryDto>(json.encodeToString(dto)))
    }

    @Test
    fun auditLogPageResponse_roundTrip_lastPage() {
        // nextBefore == null segnala l'ultima pagina (meno righe di quante richieste) —
        // vedi il commento sulla data class in AuditLogDto.kt.
        val dto = AuditLogPageResponse(
            entries = listOf(
                AuditLogEntryDto("log-1", "user-1", "a@b.com", "LOGIN", null, null, null, null, 1000L)
            ),
            nextBefore = null
        )
        assertEquals(dto, json.decodeFromString<AuditLogPageResponse>(json.encodeToString(dto)))
        assertNull(json.decodeFromString<AuditLogPageResponse>(json.encodeToString(dto)).nextBefore)
    }

    @Test
    fun auditLogPageResponse_roundTrip_hasMorePages() {
        val dto = AuditLogPageResponse(entries = emptyList(), nextBefore = 5000L)
        val decoded = json.decodeFromString<AuditLogPageResponse>(json.encodeToString(dto))
        assertEquals(5000L, decoded.nextBefore)
    }
}
