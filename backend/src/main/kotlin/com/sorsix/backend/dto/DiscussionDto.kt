package com.sorsix.backend.dto

import com.sorsix.backend.domain.discussions.Comment
import com.sorsix.backend.domain.discussions.Discussion
import java.time.Instant

data class DiscussionDto(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val userId: Long,
    val caseId: Long,
    val commentsCount: Int,
)

fun Discussion.toDiscussionDto() = DiscussionDto(
    id = this.id!!,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt,
    userId = this.user.id,
    caseId = this.publicCase.id!!,
    commentsCount = countComments(this.comments),
)

fun countComments(comments: List<Comment>): Int {
    fun recursiveCount(list: List<Comment>): Int =
        list.sumOf { 1 + recursiveCount(it.replies) }

    return recursiveCount(comments.filter { it.parent == null })
}