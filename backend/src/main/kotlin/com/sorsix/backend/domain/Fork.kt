package com.sorsix.backend.domain

import jakarta.persistence.*

@Entity
@Table(name = "forks")
data class Fork(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "fork_title", nullable = false, length = 200)
    val forkTitle: String,

    @Column(name = "fork_description", columnDefinition = "TEXT")
    val forkDescription: String?,

    @Column(name = "alternative_diagnosis", columnDefinition = "TEXT")
    val alternativeDiagnosis: String?,

    @Column(name = "alternative_treatment", columnDefinition = "TEXT")
    val alternativeTreatment: String?,

    @Column(name = "analysis_notes", columnDefinition = "TEXT")
    val analysisNotes: String?,

    @Column(name = "recommendations", columnDefinition = "TEXT")
    val recommendations: String?,

    @ManyToOne
    @JoinColumn(name = "case_id")
    val origin: Case,

    @OneToOne
    @JoinColumn(name = "doctor_id")
    val editor: Doctor
)

