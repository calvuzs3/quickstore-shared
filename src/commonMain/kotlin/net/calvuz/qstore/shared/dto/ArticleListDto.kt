package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

// Read model per GET /articles (quickstore-server) — distinto da ArticleDto in
// SyncDto.kt, che è il payload di sync grezzo (nessun campo denormalizzato/calcolato).
// categoryName è denormalizzato per evitare un secondo giro di rete lato client;
// totalQuantity è calcolato dal server aggregando movements (nessuna tabella
// inventory lato server, vedi quickstore-server CLAUDE.md).

@Serializable
data class ArticleSummaryDto(
    val id: String,
    val name: String,
    val categoryId: String,
    val categoryName: String,
    val unitOfMeasure: String,
    val codeOem: String,
    val codeErp: String,
    val codeBm: String,
    val reorderLevel: Double,
    val totalQuantity: Double
)

@Serializable
data class ArticleListResponse(
    val items: List<ArticleSummaryDto>,
    val total: Int
)
