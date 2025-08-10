package com.sorsix.backend.dto.security

import com.sorsix.backend.domain.enums.UserRole

data class JwtResponse(
    val accessToken: String,
    val type: String = "Bearer",
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole
)
