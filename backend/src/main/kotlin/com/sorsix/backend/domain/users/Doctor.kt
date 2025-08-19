package com.sorsix.backend.domain.users

import com.sorsix.backend.domain.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "doctors")
@DiscriminatorValue("DOCTOR")
class Doctor(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    phoneNumber: String? = null,

    @Column(name = "specialization", nullable = false, length = 100)
    val specialization: String,

    @Column(name = "department", length = 100)
    val department: String?,
) : User(
    email = email,
    password = password,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    role = UserRole.DOCTOR,
)