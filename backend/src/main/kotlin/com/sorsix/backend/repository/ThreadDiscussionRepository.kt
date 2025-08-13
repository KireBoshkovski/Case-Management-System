package com.sorsix.backend.repository

import com.sorsix.backend.domain.ThreadDiscussion
import org.springframework.data.jpa.repository.JpaRepository

interface ThreadDiscussionRepository : JpaRepository<ThreadDiscussion, Long> {
    //TODO
    fun findByThreadIdOrderByCreatedAtAsc(threadId: Long): List<ThreadDiscussion>
}