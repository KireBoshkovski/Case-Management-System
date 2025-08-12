package com.sorsix.backend.domain

import com.sorsix.backend.dto.AttachmentResponse
import com.sorsix.backend.domain.users.Doctor
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "thread_attachments")
data class ThreadAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    val id: Long = 0,

    @ManyToOne(optional = false) @JoinColumn(name = "thread_id")
    val thread: MedicalThread,

    @Column(name = "file_name", nullable = false, length = 255)
    val fileName: String,

    @Column(name = "content_type", nullable = false, length = 100)
    val contentType: String,

    // Public or signed URL to file
    @Column(name = "url", nullable = false, length = 1000)
    val url: String,

    @Column(name = "uploaded_at", nullable = false)
    val uploadedAt: LocalDateTime = LocalDateTime.now()
)


fun ThreadAttachment.toResponse() = AttachmentResponse(
    id = id,
    threadId = thread.id,
    fileName = fileName,
    contentType = contentType,
    url = url,
    uploadedAt = uploadedAt
)