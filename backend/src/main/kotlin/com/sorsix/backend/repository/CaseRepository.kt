package com.sorsix.backend.repository

import com.sorsix.backend.domain.Case
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CaseRepository : JpaRepository<Case, Long> {

    @EntityGraph(attributePaths = ["patient", "doctor"])
    override fun findAll(pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAllByPublicTrue(pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAllByPublicFalse(pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAllByPatientId(patientId: Long, pageable: Pageable): Page<Case>
}
