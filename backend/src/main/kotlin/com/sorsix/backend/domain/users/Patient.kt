package com.sorsix.backend.domain.users

import com.sorsix.backend.domain.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "patients")
@DiscriminatorValue("PATIENT")
class Patient(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    phoneNumber: String? = null,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Column(name = "gender", length = 10)
    val gender: String? = null,

    @Column(name = "address", columnDefinition = "TEXT")
    val address: String? = null,
) : User(
    email = email,
    password = password,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    role = UserRole.PATIENT
)