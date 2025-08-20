package com.sorsix.backend.dto

import com.sorsix.backend.domain.discussions.Discussion
import java.time.Instant

data class DiscussionDto(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val userId: Long,
    val caseId: Long,
)

fun Discussion.toDiscussionDto() = DiscussionDto(
    id = this.id!!,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt,
    userId = this.user.id,
    caseId = this.publicCase.id!!
)