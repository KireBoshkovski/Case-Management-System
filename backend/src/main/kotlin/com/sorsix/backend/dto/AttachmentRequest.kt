package com.sorsix.backend.dto

data class AttachmentRequest(
    val threadId: Long,
    val fileName: String,
    val contentType: String,
    val url: String
)