package com.sorsix.backend.dto

import java.time.LocalDateTime

data class AttachmentResponse(
    val id: Long,
    val threadId: Long,
    val fileName: String,
    val contentType: String,
    val url: String,
    val uploadedAt: LocalDateTime
)
