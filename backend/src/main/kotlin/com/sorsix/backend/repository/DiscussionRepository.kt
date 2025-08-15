package com.sorsix.backend.repository

import com.sorsix.backend.domain.discussions.Discussion
import org.springframework.data.jpa.repository.JpaRepository

interface DiscussionRepository : JpaRepository<Discussion, Long> {
    fun findAllByPublicCaseId(caseId: Long): List<Discussion>
}