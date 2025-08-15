package com.sorsix.backend.dto

import java.time.Instant

data class CommentDto(
    val id: Long?,
    val content: String,
    val createdAt: Instant,
    val userId: Long,
    val discussionId: Long,
    val parentId: Long?,
    val replies: List<CommentDto> = emptyList()
)