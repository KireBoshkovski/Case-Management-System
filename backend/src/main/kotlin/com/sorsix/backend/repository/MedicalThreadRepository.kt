package com.sorsix.backend.repository

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.domain.enums.ThreadStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MedicalThreadRepository : JpaRepository<MedicalThread, Long> {
    fun findByStatus(active: ThreadStatus): List<MedicalThread>
}