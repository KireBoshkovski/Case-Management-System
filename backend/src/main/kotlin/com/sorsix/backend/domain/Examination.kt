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
    val id: Long? = null,

    @Column(name = "examination_type", nullable = false, length = 100)
    val examinationType: String,

    @Column(name = "findings", columnDefinition = "TEXT")
    val findings: String? = null,

    @Column(name = "results", columnDefinition = "TEXT")
    val results: String? = null,

    @Column(name = "notes", columnDefinition = "TEXT")
    val notes: String? = null,

    @Column(name = "vital_signs", columnDefinition = "JSON")
    val vitalSigns: String? = null,

    @Column(name = "examination_date", nullable = false)
    val examinationDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "doctor_id", unique = false)
    val doctor: Doctor,
)
