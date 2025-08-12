package com.sorsix.backend.dto

import java.time.LocalDateTime

data class DiscussionResponse(
    val id: Long,
    val content: String,
    val diagnosisSuggestion: String?,
    val confidenceLevel: Int?,
    val createdAt: LocalDateTime,
    val threadId: Long,
    val doctorId: Long,
    val parentDiscussionId: Long?
)