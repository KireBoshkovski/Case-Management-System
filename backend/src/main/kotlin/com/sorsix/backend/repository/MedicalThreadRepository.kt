package com.sorsix.backend.repository

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.domain.enums.ThreadStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MedicalThreadRepository : JpaRepository<MedicalThread, Long> {
    fun findByStatus(status: ThreadStatus, pageable: Pageable): Page<MedicalThread>
    fun findByParentThreadId(parentThreadId: Long, pageable: Pageable): Page<MedicalThread>
}