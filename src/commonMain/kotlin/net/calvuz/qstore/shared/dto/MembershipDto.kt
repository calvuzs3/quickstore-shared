package net.calvuz.qstore.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class MembershipDto(
    val id: String,
    val userId: String,
    val email: String,
    val displayName: String?,
    val roleLevel: Int,
    val roleCode: String
)

@Serializable
data class InviteMembershipRequest(val email: String, val roleLevel: Int)

@Serializable
data class UpdateMembershipRoleRequest(val roleLevel: Int)
