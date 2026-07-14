package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Questi DTO sono l'unico contratto di rete tra QuickStore (Android) e quickstore-server,
 * estratti qui da due copie duplicate a mano che dovevano essere tenute sincronizzate
 * manualmente. Un nome di campo cambiato per errore in questo file rompe silenziosamente
 * l'interoperabilità tra app e server — questi test bloccano proprio quella regressione,
 * verificando sia il round-trip sia la shape JSON esatta (nomi/ordine dei campi) attesa
 * dal server (vedi quickstore-server CLAUDE.md sezione 3 "Dominio").
 */
class SyncDtoTest {

    private val json = Json

    @Test
    fun articleCategoryDto_roundTrip() {
        val dto = ArticleCategoryDto(
            id = "cat-1", name = "Viti", description = "desc", notes = "note",
            createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        assertEquals(dto, json.decodeFromString<ArticleCategoryDto>(json.encodeToString(dto)))
    }

    @Test
    fun articleCategoryDto_jsonShape() {
        val dto = ArticleCategoryDto(
            id = "cat-1", name = "Viti", description = "desc", notes = "note",
            createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        val expected = """{"id":"cat-1","name":"Viti","description":"desc","notes":"note","createdAt":1000,"updatedAt":2000,"isDeleted":false}"""
        assertEquals(expected, json.encodeToString(dto))
    }

    @Test
    fun articleDto_roundTrip() {
        val dto = ArticleDto(
            id = "art-1", name = "Bullone M6", description = "desc", categoryId = "cat-1",
            unitOfMeasure = "pz", reorderLevel = 10.0, notes = "note",
            codeOem = "OEM1", codeErp = "ERP1", codeBm = "BM1",
            createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        assertEquals(dto, json.decodeFromString<ArticleDto>(json.encodeToString(dto)))
    }

    @Test
    fun articleDto_jsonShape() {
        val dto = ArticleDto(
            id = "art-1", name = "Bullone M6", description = "desc", categoryId = "cat-1",
            unitOfMeasure = "pz", reorderLevel = 10.0, notes = "note",
            codeOem = "OEM1", codeErp = "ERP1", codeBm = "BM1",
            createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        val expected = """{"id":"art-1","name":"Bullone M6","description":"desc","categoryId":"cat-1","unitOfMeasure":"pz","reorderLevel":10.0,"notes":"note","codeOem":"OEM1","codeErp":"ERP1","codeBm":"BM1","createdAt":1000,"updatedAt":2000,"isDeleted":false}"""
        assertEquals(expected, json.encodeToString(dto))
    }

    @Test
    fun movementDto_roundTrip_withNullableLocations() {
        val inMovement = MovementDto(
            id = "mov-1", articleId = "art-1", type = "IN",
            fromLocationId = null, toLocationId = "loc-1",
            quantity = 5.0, notes = "", createdBy = "user-1", createdAt = 1000L
        )
        assertEquals(inMovement, json.decodeFromString<MovementDto>(json.encodeToString(inMovement)))

        val transfer = inMovement.copy(type = "TRANSFER", fromLocationId = "loc-2", toLocationId = "loc-1")
        assertEquals(transfer, json.decodeFromString<MovementDto>(json.encodeToString(transfer)))
    }

    @Test
    fun movementDto_jsonShape() {
        val dto = MovementDto(
            id = "mov-1", articleId = "art-1", type = "OUT",
            fromLocationId = "loc-1", toLocationId = null,
            quantity = 5.0, notes = "note", createdBy = "user-1", createdAt = 1000L
        )
        val expected = """{"id":"mov-1","articleId":"art-1","type":"OUT","fromLocationId":"loc-1","toLocationId":null,"quantity":5.0,"notes":"note","createdBy":"user-1","createdAt":1000}"""
        assertEquals(expected, json.encodeToString(dto))
    }

    @Test
    fun articleImageDto_roundTrip() {
        val dto = ArticleImageDto(
            id = "img-1", articleId = "art-1", imagePath = "art-1/photo.jpg",
            featuresData = "base64==", createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        assertEquals(dto, json.decodeFromString<ArticleImageDto>(json.encodeToString(dto)))
    }

    @Test
    fun locationDto_roundTrip() {
        val dto = LocationDto(
            id = "loc-1", name = "Sede", notes = "note",
            createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        assertEquals(dto, json.decodeFromString<LocationDto>(json.encodeToString(dto)))
    }

    @Test
    fun articleLocationThresholdDto_roundTrip() {
        val dto = ArticleLocationThresholdDto(
            id = "th-1", articleId = "art-1", locationId = "loc-1", reorderLevel = 3.0,
            createdAt = 1000L, updatedAt = 2000L, isDeleted = false
        )
        assertEquals(dto, json.decodeFromString<ArticleLocationThresholdDto>(json.encodeToString(dto)))
    }

    @Test
    fun syncPushRequest_roundTrip_withDefaults() {
        // deviceId è l'unico campo obbligatorio: tutte le liste hanno default emptyList(),
        // il device pusha solo le entità che hanno effettivamente qualcosa da mandare.
        val dto = SyncPushRequest(deviceId = "device-1")
        assertEquals(dto, json.decodeFromString<SyncPushRequest>(json.encodeToString(dto)))
        assertEquals("""{"deviceId":"device-1"}""", json.encodeToString(dto))
    }

    @Test
    fun syncPushRequest_roundTrip_populated() {
        val dto = SyncPushRequest(
            deviceId = "device-1",
            articleCategories = listOf(
                ArticleCategoryDto("cat-1", "Viti", "", "", 1000L, 2000L, false)
            ),
            locations = listOf(LocationDto("loc-1", "Sede", "", 1000L, 2000L, false))
        )
        assertEquals(dto, json.decodeFromString<SyncPushRequest>(json.encodeToString(dto)))
    }

    @Test
    fun syncPushResponse_roundTrip() {
        val dto = SyncPushResponse(
            acceptedIds = listOf("id-1", "id-2"),
            rejectedIds = listOf(RejectedEntry("id-3", "articleId inesistente")),
            serverTimestamp = 12345L
        )
        assertEquals(dto, json.decodeFromString<SyncPushResponse>(json.encodeToString(dto)))
    }

    @Test
    fun syncPullResponse_roundTrip_empty() {
        val dto = SyncPullResponse(
            serverTimestamp = 12345L,
            articleCategories = emptyList(),
            locations = emptyList(),
            articles = emptyList(),
            articleLocationThresholds = emptyList(),
            movements = emptyList(),
            articleImages = emptyList()
        )
        assertEquals(dto, json.decodeFromString<SyncPullResponse>(json.encodeToString(dto)))
    }

    @Test
    fun syncPullResponse_decodesServerGoldenPayload() {
        // Payload congelato nella forma esatta prodotta da quickstore-server (SyncRoutes.kt
        // GET /sync/pull) — se un domani il server rinomina/riordina un campo senza toccare
        // questo modulo condiviso, questo test smette di compilare/decodificare correttamente.
        val golden = """
            {
              "serverTimestamp": 999,
              "articleCategories": [],
              "locations": [{"id":"loc-1","name":"Sede","notes":"","createdAt":1,"updatedAt":2,"isDeleted":false}],
              "articles": [],
              "articleLocationThresholds": [],
              "movements": [{"id":"mov-1","articleId":"art-1","type":"IN","fromLocationId":null,"toLocationId":"loc-1","quantity":1.0,"notes":"","createdBy":"user-1","createdAt":5}],
              "articleImages": []
            }
        """.trimIndent()
        val decoded = json.decodeFromString<SyncPullResponse>(golden)
        assertEquals(999L, decoded.serverTimestamp)
        assertEquals("Sede", decoded.locations.single().name)
        assertEquals("IN", decoded.movements.single().type)
    }
}
