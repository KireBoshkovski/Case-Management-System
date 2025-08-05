package com.sorsix.backend.domain.users

import com.sorsix.backend.domain.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "patient_id")
class Patient(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    phoneNumber: String? = null,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Column(name = "gender", length = 10)
    val gender: String?,

    @Column(name = "address", columnDefinition = "TEXT")
    val address: String?,
) : User(
    email = email,
    password = password,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    role = UserRole.PATIENT
) {
    constructor() : this(
        email = "",
        password = "",
        firstName = "",
        lastName = "",
        phoneNumber = null,
        dateOfBirth = LocalDate.now(),
        gender = null,
        address = null,
    )
}
