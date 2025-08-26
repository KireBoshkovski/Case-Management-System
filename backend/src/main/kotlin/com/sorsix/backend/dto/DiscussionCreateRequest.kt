package com.sorsix.backend.dto

data class DiscussionCreateRequest(
    val title: String,
    val description: String,
    val caseId: Long
)