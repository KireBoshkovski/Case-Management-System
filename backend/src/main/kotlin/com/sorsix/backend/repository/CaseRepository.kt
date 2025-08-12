package com.sorsix.backend.repository

import com.sorsix.backend.domain.Case
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CaseRepository : JpaRepository<Case, Long> {
    fun findAllByPatientId(patientId: Long): List<Case>

    fun findAllByDoctorId(doctorId: Long): List<Case>
}