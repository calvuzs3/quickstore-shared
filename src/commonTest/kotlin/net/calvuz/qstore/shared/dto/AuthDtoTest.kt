package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Lato QuickStore (Android) queste classi avevano un suffisso "Dto" che qui è stato
 * tolto per convenzione col server (LoginRequestDto -> LoginRequest, ecc., vedi
 * QuickStore CLAUDE.md sezione "Auth"). I golden JSON verificano che il rename dei nomi
 * di classe Kotlin non abbia toccato i nomi di campo effettivamente serializzati — è
 * quello che conta per l'interoperabilità col server, non il nome della classe.
 */
class AuthDtoTest {

    private val json = Json

    @Test
    fun loginRequest_roundTrip() {
        val dto = LoginRequest(email = "a@b.com", password = "secret")
        assertEquals(dto, json.decodeFromString<LoginRequest>(json.encodeToString(dto)))
        assertEquals("""{"email":"a@b.com","password":"secret"}""", json.encodeToString(dto))
    }

    @Test
    fun loginResponse_roundTrip() {
        val dto = LoginResponse(
            token = "jwt-token", orgId = "org-1", orgName = "Org", roleLevel = 9, roleCode = "ADMIN"
        )
        assertEquals(dto, json.decodeFromString<LoginResponse>(json.encodeToString(dto)))
        val expected = """{"token":"jwt-token","orgId":"org-1","orgName":"Org","roleLevel":9,"roleCode":"ADMIN"}"""
        assertEquals(expected, json.encodeToString(dto))
    }

    @Test
    fun loginOrgChoiceResponse_roundTrip() {
        val dto = LoginOrgChoiceResponse(
            pendingToken = "pending-token",
            organizations = listOf(
                OrganizationSummaryDto("org-1", "Org 1", 9, "ADMIN"),
                OrganizationSummaryDto("org-2", "Org 2", 1, "MEMBER")
            )
        )
        assertEquals(dto, json.decodeFromString<LoginOrgChoiceResponse>(json.encodeToString(dto)))
    }

    @Test
    fun loginOrgChoiceResponse_decodesServerGoldenPayload() {
        // Forma esatta usata da AuthApi.kt per distinguere le due risposte di /auth/login
        // guardando la presenza della chiave "pendingToken" (nessun discriminatore esplicito).
        val golden = """{"pendingToken":"pending-1","organizations":[{"id":"org-1","name":"Org","roleLevel":9,"roleCode":"ADMIN"}]}"""
        val decoded = json.decodeFromString<LoginOrgChoiceResponse>(golden)
        assertEquals("pending-1", decoded.pendingToken)
        assertEquals(1, decoded.organizations.size)
    }

    @Test
    fun selectOrgRequest_roundTrip() {
        val dto = SelectOrgRequest(orgId = "org-1")
        assertEquals(dto, json.decodeFromString<SelectOrgRequest>(json.encodeToString(dto)))
        assertEquals("""{"orgId":"org-1"}""", json.encodeToString(dto))
    }
}
