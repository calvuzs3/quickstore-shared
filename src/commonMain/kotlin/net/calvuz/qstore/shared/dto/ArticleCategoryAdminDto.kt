package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

// Creazione/modifica/eliminazione categoria (POST/PUT/DELETE /article-categories)
// dal pannello web — stessa tabella article_categories toccata da /sync/push,
// nessuna migrazione. Risposta: ArticleCategoryDto già esistente in SyncDto.kt
// (nessun campo calcolato/denormalizzato come per gli articoli, non serve un
// nuovo "summary" DTO).

@Serializable
data class CreateArticleCategoryRequest(
    val name: String,
    val description: String = "",
    val notes: String = ""
)

@Serializable
data class UpdateArticleCategoryRequest(
    val name: String,
    val description: String,
    val notes: String
)
