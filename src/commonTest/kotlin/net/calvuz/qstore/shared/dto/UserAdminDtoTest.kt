package net.calvuz.qstore.shared.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class UserAdminDtoTest {

    private val json = Json

    @Test
    fun createUserRequest_roundTrip_withDefaultDisplayName() {
        val dto = CreateUserRequest(email = "mario@example.com", password = "supersegreta")
        assertEquals(dto, json.decodeFromString<CreateUserRequest>(json.encodeToString(dto)))
        assertEquals("""{"email":"mario@example.com","password":"supersegreta"}""", json.encodeToString(dto))
    }

    @Test
    fun createUserRequest_roundTrip_withDisplayName() {
        val dto = CreateUserRequest(email = "mario@example.com", password = "supersegreta", displayName = "Mario Rossi")
        assertEquals(dto, json.decodeFromString<CreateUserRequest>(json.encodeToString(dto)))
    }

    @Test
    fun userDto_roundTrip() {
        val dto = UserDto(id = "user-1", email = "mario@example.com", displayName = null)
        assertEquals(dto, json.decodeFromString<UserDto>(json.encodeToString(dto)))
    }
}
