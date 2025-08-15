package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.Case
import org.springframework.data.jpa.repository.JpaRepository

interface CaseRepository : JpaRepository<Case, Long> {
    fun findAllByPatientId(patientId: Long): List<Case>

    fun findAllByDoctorId(doctorId: Long): List<Case>
}