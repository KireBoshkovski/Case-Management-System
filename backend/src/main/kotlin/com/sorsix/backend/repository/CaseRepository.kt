package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.Case
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface CaseRepository : JpaRepository<Case, Long>, JpaSpecificationExecutor<Case> {
     @Query("""
        SELECT DISTINCT c FROM Case c
        LEFT JOIN FETCH c.doctor d
        LEFT JOIN FETCH c.patient p
        LEFT JOIN FETCH c.examinations e
        LEFT JOIN FETCH e.doctor ed
        WHERE c.doctor.id = :doctorId
        ORDER BY c.createdAt DESC
    """)
    fun findByDoctorIdWithDetails(doctorId: Long, pageable: Pageable): Page<Case>
}