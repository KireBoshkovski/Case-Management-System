package com.sorsix.backend.dto

import com.sorsix.backend.domain.enums.UserRole

data class JwtResponse(
    val accessToken: String,
    val refreshToken: String,
    val type: String = "Bearer",
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole
)
