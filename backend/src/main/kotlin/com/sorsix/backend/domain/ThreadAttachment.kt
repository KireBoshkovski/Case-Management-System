package com.sorsix.backend.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "thread_attachments")
data class ThreadAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    val id: Long = 0,

    @Column(name = "file_name", nullable = false, length = 255)
    val fileName: String,

    @Column(name = "file_path", nullable = false, length = 500)
    val filePath: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String?,

    @Column(name = "uploaded_at", nullable = false)
    val uploadedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    val thread: MedicalThread,

    @ManyToOne
    @JoinColumn(name = "uploaded_by_doctor_id", nullable = false)
    val uploadedBy: Doctor,
)