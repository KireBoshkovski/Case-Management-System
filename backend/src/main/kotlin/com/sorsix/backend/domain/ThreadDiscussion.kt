package com.sorsix.backend.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "thread_discussions")
data class ThreadDiscussion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discussion_id")
    val id: Long = 0,

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    val content: String,

    @Column(name = "diagnosis_suggestion", columnDefinition = "TEXT")
    val diagnosisSuggestion: String?,

    @Column(name = "confidence_level")
    val confidenceLevel: Int?, // 1-10 scale

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    val thread: MedicalThread,

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    val doctor: Doctor,

    @ManyToOne
    @JoinColumn(name = "parent_discussion_id")
    val parentDiscussion: ThreadDiscussion? = null, // For replies

    @OneToMany(mappedBy = "parentDiscussion", fetch = FetchType.LAZY)
    val replies: MutableList<ThreadDiscussion> = mutableListOf()
)