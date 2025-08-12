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

    @EntityGraph(attributePaths = ["patient", "doctor"])
    override fun findAll(pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAll(spec: Specification<Case>?, pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAllByPublicTrue(pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAllByPublicFalse(pageable: Pageable): Page<Case>

    @EntityGraph(attributePaths = ["patient", "doctor"])
    fun findAllByPatientId(patientId: Long, pageable: Pageable): Page<Case>

    @Query(
        """
    SELECT c FROM Case c
    WHERE (:patientId IS NULL OR c.patient.id = :patientId)
      AND (:publicVal IS NULL OR c.public = :publicVal)
      AND (
            :q IS NULL OR
            LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(c.allergies)   LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(c.bloodType)   LIKE LOWER(CONCAT('%', :q, '%'))
      )
      AND (:qid IS NULL OR c.id = :qid)
    """
    )
    fun searchCases(
        @Param("patientId") patientId: Long?,
        @Param("publicVal") publicVal: Boolean?,
        @Param("q") q: String?,
        @Param("qid") qid: Long?,
        pageable: Pageable
    ): Page<Case>


}
