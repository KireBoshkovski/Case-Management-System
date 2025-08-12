package com.sorsix.backend.dto

data class CreateDiscussionRequest(
    val threadId: Long,
    val doctorId: Long,
    val content: String,
    val diagnosisSuggestion: String? = null,
    val confidenceLevel: Int? = null, //1-10
    val parentDiscussionId: Long? = null
)