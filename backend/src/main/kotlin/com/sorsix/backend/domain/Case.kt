package com.sorsix.backend.domain

import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.domain.users.Doctor
import com.sorsix.backend.domain.users.Patient
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "cases")
data class Case(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    val id: Long,

    @Column(name = "blood_type", length = 5)
    val bloodType: String?,

    @Column(name = "allergies", columnDefinition = "TEXT")
    val allergies: String?,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String?,

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    val treatmentPlan: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: CaseStatus = CaseStatus.ACTIVE,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "patient_id")
    val patient: Patient,

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    val doctor: Doctor,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    val examinations: MutableList<Examination> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_case_id")
    val publishedCase: PublicCase?,
)
