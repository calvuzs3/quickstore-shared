package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Read model per GET /articles (quickstore-server) — vedi ArticleListDto.kt. Stesso
 * motivo dei test in SyncDtoTest.kt: bloccare una regressione silenziosa nella shape
 * JSON tra server (Kotlin) e i client di lettura (web TypeScript, mai lo stesso
 * compilatore a garantire la corrispondenza dei nomi campo).
 */
class ArticleListDtoTest {

    private val json = Json

    private fun sampleSummary() = ArticleSummaryDto(
        id = "art-1",
        name = "Bullone M6",
        categoryId = "cat-1",
        categoryName = "Viti",
        unitOfMeasure = "pz",
        codeOem = "OEM1",
        codeErp = "ERP1",
        codeBm = "BM1",
        reorderLevel = 10.0,
        totalQuantity = 25.0
    )

    @Test
    fun articleSummaryDto_roundTrip() {
        val dto = sampleSummary()
        assertEquals(dto, json.decodeFromString<ArticleSummaryDto>(json.encodeToString(dto)))
    }

    @Test
    fun articleSummaryDto_jsonShape() {
        val dto = sampleSummary()
        val expected = """{"id":"art-1","name":"Bullone M6","categoryId":"cat-1","categoryName":"Viti","unitOfMeasure":"pz","codeOem":"OEM1","codeErp":"ERP1","codeBm":"BM1","reorderLevel":10.0,"totalQuantity":25.0}"""
        assertEquals(expected, json.encodeToString(dto))
    }

    @Test
    fun articleListResponse_roundTrip_empty() {
        val dto = ArticleListResponse(items = emptyList(), total = 0)
        assertEquals(dto, json.decodeFromString<ArticleListResponse>(json.encodeToString(dto)))
    }

    @Test
    fun articleListResponse_roundTrip_populated() {
        val dto = ArticleListResponse(items = listOf(sampleSummary(), sampleSummary().copy(id = "art-2")), total = 2)
        assertEquals(dto, json.decodeFromString<ArticleListResponse>(json.encodeToString(dto)))
    }

    @Test
    fun articleListResponse_decodesServerGoldenPayload() {
        // Forma esatta prodotta da ArticleServerRepository.list() (ArticleRoutes.kt
        // GET /articles) — se il server rinomina/riordina un campo senza toccare
        // questo modulo condiviso, questo test smette di decodificare correttamente.
        val golden = """
            {
              "items": [{"id":"art-1","name":"Bullone M6","categoryId":"cat-1","categoryName":"Viti","unitOfMeasure":"pz","codeOem":"OEM1","codeErp":"ERP1","codeBm":"BM1","reorderLevel":10.0,"totalQuantity":25.0}],
              "total": 1
            }
        """.trimIndent()
        val decoded = json.decodeFromString<ArticleListResponse>(golden)
        assertEquals(1, decoded.total)
        assertEquals("Viti", decoded.items.single().categoryName)
        assertEquals(25.0, decoded.items.single().totalQuantity)
    }
}
