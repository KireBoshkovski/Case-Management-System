package com.sorsix.backend.domain

import com.sorsix.backend.dto.DiscussionResponse
import com.sorsix.backend.domain.users.Doctor
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
    val diagnosisSuggestion: String? = null,

    @Column(name = "confidence_level")
    val confidenceLevel: Int? = null, // 1–10

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(optional = false) @JoinColumn(name = "thread_id")
    val thread: MedicalThread,

    @ManyToOne(optional = false) @JoinColumn(name = "doctor_id")
    val doctor: Doctor,

    @ManyToOne @JoinColumn(name = "parent_discussion_id")
    val parentDiscussion: ThreadDiscussion? = null
)

fun ThreadDiscussion.toResponse() = DiscussionResponse(
    id = id,
    content = content,
    diagnosisSuggestion = diagnosisSuggestion,
    confidenceLevel = confidenceLevel,
    createdAt = createdAt,
    threadId = thread.id,
    doctorId = doctor.id,
    parentDiscussionId = parentDiscussion?.id
)