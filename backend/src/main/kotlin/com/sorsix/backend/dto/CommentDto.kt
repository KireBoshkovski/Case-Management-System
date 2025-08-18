package com.sorsix.backend.dto

import com.sorsix.backend.domain.discussions.Comment
import java.time.Instant

data class CommentDto(
    val id: Long?,
    val content: String,
    val createdAt: Instant = Instant.now(),
    val userId: Long?,
    val discussionId: Long,
    val parentId: Long?,
    val replies: List<CommentDto> = emptyList()
)

fun Comment.toDto(): CommentDto = CommentDto(
    id = this.id,
    content = this.content,
    createdAt = this.createdAt,
    userId = this.user.id,
    discussionId = this.discussion.id!!,
    parentId = this.parent?.id,
    replies = this.replies.map { it.toDto() }
)