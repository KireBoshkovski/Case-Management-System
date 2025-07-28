package com.sorsix.backend.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "patients")
data class Patient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    val id: Long,

    @Column(name = "first_name", nullable = false, length = 100)
    val firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    val lastName: String,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Column(name = "gender", length = 10)
    val gender: String,

    @Column(name = "phone_number", length = 20)
    val phoneNumber: String?,

    @Column(name = "email", length = 50)
    val email: String?,

    @Column(name = "address", columnDefinition = "TEXT")
    val address: String?,

    )
