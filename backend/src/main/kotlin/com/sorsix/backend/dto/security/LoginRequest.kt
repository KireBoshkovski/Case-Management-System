package com.sorsix.backend.dto.security

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(min = 5, max = 40)
    val password: String
)