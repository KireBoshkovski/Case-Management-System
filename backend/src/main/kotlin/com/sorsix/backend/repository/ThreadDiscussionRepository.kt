package com.sorsix.backend.repository

import com.sorsix.backend.domain.ThreadDiscussion
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ThreadDiscussionRepository : JpaRepository<ThreadDiscussion, Long> {
    fun findByThreadIdOrderByCreatedAtAsc(threadId: Long, pageable: Pageable): Page<ThreadDiscussion>
}