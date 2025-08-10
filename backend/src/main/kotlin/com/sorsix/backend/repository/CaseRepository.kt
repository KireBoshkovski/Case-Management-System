package com.sorsix.backend.repository

import com.sorsix.backend.domain.Case
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CaseRepository : JpaRepository<Case, Long> {
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

    fun findAllByPatientId(patientId: Long): List<Case>

    fun findAllByDoctorId(doctorId: Long): List<Case>
}