package com.sorsix.backend.dto

import com.sorsix.backend.domain.enums.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class SignUpRequest(
    @field:NotBlank
    @field:Size(max = 50)
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 40)
    val password: String,

    @field:NotBlank
    @field:Size(max = 20)
    val firstName: String,

    @field:NotBlank
    @field:Size(max = 20)
    val lastName: String,

    @field:NotNull
    val role: UserRole,

    // Optional fields for Patient
    val dateOfBirth: LocalDate? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val gender: String? = null,

    // Optional fields for Doctor
    val specialization: String? = null,
    val department: String? = null
)
