package com.sorsix.backend.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity
@Table(name = "public_examinations")
data class PublicExamination(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_examination_id")
    val id: Long,

    // Reference to original examination for audit purposes
    @Column(name = "original_examination_id", nullable = false)
    val originalExaminationId: Long,

    @Column(name = "examination_type", nullable = false, length = 100)
    val examinationType: String,

    @Column(name = "findings", columnDefinition = "TEXT")
    val findings: String?,

    @Column(name = "results", columnDefinition = "TEXT")
    val results: String?,

    @Column(name = "notes", columnDefinition = "TEXT")
    val notes: String?,

    @Column(name = "vital_signs", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    val vitalSigns: String?,

    @Column(name = "examination_date")
    val examinationDate: LocalDateTime?,

    @Column(name = "examining_doctor_specialty", length = 100)
    val examiningDoctorSpecialty: String?,

    @Column(name = "published_at", nullable = false)
    val publishedAt: LocalDateTime = LocalDateTime.now(),
)