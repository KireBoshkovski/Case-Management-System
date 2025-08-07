package com.sorsix.backend.domain

import com.sorsix.backend.domain.enums.ThreadStatus
import com.sorsix.backend.domain.users.Doctor
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "medical_threads")
data class MedicalThread(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thread_id")
    val id: Long = 0,

    @Column(name = "title", nullable = false, length = 200)
    val title: String,

    @Column(name = "anonymized_symptoms", columnDefinition = "TEXT", nullable = false)
    val anonymizedSymptoms: String,

    @Column(name = "anonymized_patient_info", columnDefinition = "TEXT")
    val anonymizedPatientInfo: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: ThreadStatus = ThreadStatus.ACTIVE,

    @Column(name = "is_educational", nullable = false)
    val isEducational: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "original_case_id")
    val originalCase: Case,

    @ManyToOne
    @JoinColumn(name = "creating_doctor_id")
    val creatingDoctor: Doctor,

    @ManyToOne
    @JoinColumn(name = "parent_thread_id")
    val parentThread: MedicalThread? = null,

    @OneToMany(mappedBy = "thread", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val discussions: MutableList<ThreadDiscussion> = mutableListOf(),

    @OneToMany(mappedBy = "thread", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val attachments: MutableList<ThreadAttachment> = mutableListOf(),

    @OneToMany(mappedBy = "parentThread", fetch = FetchType.LAZY)
    val forkedThreads: MutableList<MedicalThread> = mutableListOf()
)