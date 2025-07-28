package com.sorsix.backend.domain

import jakarta.persistence.*

@Entity
@Table(name = "doctors")
data class Doctor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    val id: Long,

    @Column(name = "first_name", nullable = false, length = 100)
    val firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    val lastName: String,

    @Column(name = "specialization", nullable = false, length = 100)
    val specialization: String,

    @Column(name = "phone_number", length = 20)
    val phoneNumber: String?,

    @Column(name = "email", nullable = false, length = 100)
    val email: String,

    @Column(name = "department", length = 100)
    val department: String?,
)
