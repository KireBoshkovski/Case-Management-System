package com.sorsix.backend.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "public_cases")
class PublicCase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_case_id")
    val id: Long,

    @Column(name = "blood_type", length = 5)
    val bloodType: String?,

    @Column(name = "allergies", columnDefinition = "TEXT")
    val allergies: String?,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String?,

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    val treatmentPlan: String?,

    @Column(name = "patient_age_range", length = 20)
    val patientAgeRange: String?, // e.g., "20-25", "30-35"

    @Column(name = "patient_gender")
    val patientGender: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "published_at")
    val publishedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "public_case_id")
    val examinations: MutableList<PublicExamination> = mutableListOf(),
)