package com.sorsix.backend.repository

import com.sorsix.backend.domain.Case
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CaseRepository : JpaRepository<Case, Long> {
    fun findAllByPublicTrue(): List<Case>
    fun findAllByPublicFalse(): List<Case>
    fun findAllByPatientId(patientId: Long): List<Case>

    @Query(
        """
    SELECT c FROM Case c 
    LEFT JOIN FETCH c.patient 
    LEFT JOIN FETCH c.doctor 
    LEFT JOIN FETCH c.examinations e
    LEFT JOIN FETCH e.doctor
    """
    )
    fun findAllWithDetails(): List<Case>

    @Query("""
        SELECT c FROM Case c 
        LEFT JOIN FETCH c.patient 
        LEFT JOIN FETCH c.doctor 
        WHERE c.id = :id
    """)
    fun findByIdWithDetails(@Param("id") id: Long): Case?


}