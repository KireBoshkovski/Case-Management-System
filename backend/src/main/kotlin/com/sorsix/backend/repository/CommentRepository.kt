package com.sorsix.backend.repository

import com.sorsix.backend.domain.discussions.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByDiscussionId(discussionId: Long): List<Comment>
}