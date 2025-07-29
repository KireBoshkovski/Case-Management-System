package com.sorsix.backend.dto

data class CreatePatientRequest(
    val firstName: String,
    val lastName: String,
    val email: String
)
