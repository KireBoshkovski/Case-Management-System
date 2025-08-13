package com.sorsix.backend.repository

import com.sorsix.backend.domain.Case
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CaseRepository : JpaRepository<Case, Long> {
    fun findAllByPatientId(patientId: Long): List<Case>

    fun findAllByDoctorId(doctorId: Long): List<Case>
}