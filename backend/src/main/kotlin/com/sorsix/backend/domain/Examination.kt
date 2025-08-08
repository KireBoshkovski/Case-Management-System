package com.sorsix.backend.domain

import com.sorsix.backend.domain.users.Doctor
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "examinations")
data class Examination(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_id")
    val id: Long,

    @Column(name = "is_public", nullable = false, updatable = false)
    val public: Boolean,

    @Column(name = "examination_type", nullable = false, length = 100)
    val examinationType: String,

    @Column(name = "findings", columnDefinition = "TEXT")
    val findings: String?,

    @Column(name = "results", columnDefinition = "TEXT")
    val results: String?,

    @Column(name = "notes", columnDefinition = "TEXT")
    val notes: String?,

    @Column(name = "vital_signs", columnDefinition = "JSON")
    val vitalSigns: String?,

    @Column(name = "examination_date", nullable = false)
    val examinationDate: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "doctor_id", unique = false)
    val doctor: Doctor,
)
