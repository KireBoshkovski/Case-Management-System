package com.sorsix.backend.dto

import com.sorsix.backend.domain.Patient

data class PatientDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String?,
)

fun Patient.toPatientDto() = PatientDto(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email
)
