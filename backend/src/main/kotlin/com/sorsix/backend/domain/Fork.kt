package com.sorsix.backend.domain

import jakarta.persistence.*

@Entity
@Table(name = "forks")
data class Fork(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,  // Add default value

    @Column(name = "fork_title", nullable = false, length = 200)
    val title: String,

    @Column(name = "fork_description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "alternative_diagnosis", columnDefinition = "TEXT")
    val alternativeDiagnosis: String? = null,

    @Column(name = "alternative_treatment", columnDefinition = "TEXT")
    val alternativeTreatment: String? = null,

    @Column(name = "analysis_notes", columnDefinition = "TEXT")
    val analysisNotes: String? = null,

    @Column(name = "recommendations", columnDefinition = "TEXT")
    val recommendations: String? = null,

    @ManyToOne
    @JoinColumn(name = "case_id")
    val origin: Case,

    @OneToOne
    @JoinColumn(name = "doctor_id")
    val editor: Doctor
)
