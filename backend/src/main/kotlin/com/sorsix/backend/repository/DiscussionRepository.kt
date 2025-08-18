package com.sorsix.backend.repository

import com.sorsix.backend.domain.discussions.Discussion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DiscussionRepository : JpaRepository<Discussion, Long>, JpaSpecificationExecutor<Discussion> {
    fun findAllByPublicCaseId(caseId: Long): List<Discussion>
}