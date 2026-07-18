package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ArticleCategoryAdminDtoTest {

    private val json = Json

    @Test
    fun createArticleCategoryRequest_roundTrip_withDefaults() {
        val dto = CreateArticleCategoryRequest(name = "Viti")
        assertEquals(dto, json.decodeFromString<CreateArticleCategoryRequest>(json.encodeToString(dto)))
        assertEquals("""{"name":"Viti"}""", json.encodeToString(dto))
    }

    @Test
    fun createArticleCategoryRequest_roundTrip_fullyPopulated() {
        val dto = CreateArticleCategoryRequest(name = "Viti", description = "desc", notes = "note")
        assertEquals(dto, json.decodeFromString<CreateArticleCategoryRequest>(json.encodeToString(dto)))
    }

    @Test
    fun updateArticleCategoryRequest_roundTrip() {
        val dto = UpdateArticleCategoryRequest(name = "Viti", description = "desc", notes = "note")
        assertEquals(dto, json.decodeFromString<UpdateArticleCategoryRequest>(json.encodeToString(dto)))
    }
}
