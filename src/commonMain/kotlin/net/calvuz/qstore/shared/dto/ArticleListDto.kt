package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

// Read model per GET /articles (quickstore-server) — distinto da ArticleDto in
// SyncDto.kt, che è il payload di sync grezzo (nessun campo denormalizzato/calcolato).
// categoryName è denormalizzato per evitare un secondo giro di rete lato client;
// totalQuantity è calcolato dal server aggregando movements (nessuna tabella
// inventory lato server, vedi quickstore-server CLAUDE.md).

// Riga di giacenza per singola ubicazione — locationName denormalizzato per lo stesso
// motivo di categoryName sopra. Solo le ubicazioni con quantità netta diversa da zero
// compaiono qui (una TRANSFER interna che si annulla non genera una riga a 0).
@Serializable
data class ArticleLocationStockDto(
    val locationId: String,
    val locationName: String,
    val quantity: Double
)

@Serializable
data class ArticleSummaryDto(
    val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val categoryName: String,
    val unitOfMeasure: String,
    val codeOem: String,
    val codeErp: String,
    val codeBm: String,
    val reorderLevel: Double,
    val notes: String,
    val totalQuantity: Double,
    // Se GET /articles è filtrato per locationId, totalQuantity è la giacenza in quella
    // sola ubicazione (non il totale su tutte); stockByLocation resta comunque la
    // ripartizione completa per ogni ubicazione con giacenza diversa da zero.
    val stockByLocation: List<ArticleLocationStockDto> = emptyList()
)

@Serializable
data class ArticleListResponse(
    val items: List<ArticleSummaryDto>,
    val total: Int
)

// Creazione/modifica anagrafica articolo (POST/PUT /articles) — non tocca la
// giacenza (si muove solo coi movimenti, non ancora scrivibili dal web) né
// article_location_thresholds. Risposta: ArticleSummaryDto (giacenza 0.0 per una
// creazione, dato che un nuovo articolo non ha ancora movimenti).

@Serializable
data class CreateArticleRequest(
    val name: String,
    val description: String = "",
    val categoryId: String,
    val unitOfMeasure: String,
    val reorderLevel: Double = 0.0,
    val notes: String = "",
    val codeOem: String = "",
    val codeErp: String = "",
    val codeBm: String = ""
)

@Serializable
data class UpdateArticleRequest(
    val name: String,
    val description: String,
    val categoryId: String,
    val unitOfMeasure: String,
    val reorderLevel: Double,
    val notes: String,
    val codeOem: String,
    val codeErp: String,
    val codeBm: String
)
