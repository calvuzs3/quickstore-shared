package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class OrganizationSummaryDto(
    val id: String,
    val name: String,
    val roleLevel: Int,
    val roleCode: String
)

/** Risposta quando l'utente ha una sola org (o dopo /auth/select-org): token pieno. */
@Serializable
data class LoginResponse(
    val token: String,
    val orgId: String,
    val orgName: String,
    val roleLevel: Int,
    val roleCode: String
)

/** Risposta quando l'utente ha più org: serve /auth/select-org prima del token pieno. */
@Serializable
data class LoginOrgChoiceResponse(
    val pendingToken: String,
    val organizations: List<OrganizationSummaryDto>
)

@Serializable
data class SelectOrgRequest(val orgId: String)
