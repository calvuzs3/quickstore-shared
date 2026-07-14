package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class MembershipDtoTest {

    private val json = Json

    @Test
    fun membershipDto_roundTrip_withNullDisplayName() {
        val dto = MembershipDto(
            id = "mem-1", userId = "user-1", email = "a@b.com",
            displayName = null, roleLevel = 1, roleCode = "MEMBER"
        )
        assertEquals(dto, json.decodeFromString<MembershipDto>(json.encodeToString(dto)))
    }

    @Test
    fun membershipDto_roundTrip_withDisplayName() {
        val dto = MembershipDto(
            id = "mem-1", userId = "user-1", email = "a@b.com",
            displayName = "Mario Rossi", roleLevel = 9, roleCode = "ADMIN"
        )
        assertEquals(dto, json.decodeFromString<MembershipDto>(json.encodeToString(dto)))
    }

    @Test
    fun inviteMembershipRequest_roundTrip() {
        val dto = InviteMembershipRequest(email = "invite@b.com", roleLevel = 5)
        assertEquals(dto, json.decodeFromString<InviteMembershipRequest>(json.encodeToString(dto)))
    }

    @Test
    fun updateMembershipRoleRequest_roundTrip() {
        val dto = UpdateMembershipRoleRequest(roleLevel = 5)
        assertEquals(dto, json.decodeFromString<UpdateMembershipRoleRequest>(json.encodeToString(dto)))
        assertEquals("""{"roleLevel":5}""", json.encodeToString(dto))
    }
}
