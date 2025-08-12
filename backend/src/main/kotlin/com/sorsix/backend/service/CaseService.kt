package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import com.sorsix.backend.specification.CaseSpecifications
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime

enum class Visibility { ALL, PUBLIC, PRIVATE }

@Service
class CaseService(
    private val caseRepository: CaseRepository
) {
    fun getCases(
        visibility: Visibility,
        patientId: Long?,
        q: String?,
        pageable: Pageable
    ): Page<Case> {
        val publicVal: Boolean? = when (visibility) {
            Visibility.PUBLIC  -> true
            Visibility.PRIVATE -> false
            Visibility.ALL     -> null
        }

        var spec: Specification<Case>? = null

        CaseSpecifications.hasPatientId(patientId)?.let {
            spec = spec?.and(it) ?: it
        }

        CaseSpecifications.isPublic(publicVal)?.let {
            spec = spec?.and(it) ?: it
        }

        CaseSpecifications.searchByTermOrId(q?.trim())?.let {
            spec = spec?.and(it) ?: it
        }

        return caseRepository.findAll(spec, pageable)
    }

    fun findById(id: Long): Case =
        caseRepository.findById(id).orElseThrow { CaseNotFoundException(id) }

    @Transactional
    fun save(case: Case): Case = caseRepository.save(case)

    @Transactional
    fun update(case: Case): Case {
        val existing = findById(case.id)
        val updated = existing.copy(
            public = case.public,
            bloodType = case.bloodType,
            allergies = case.allergies,
            description = case.description,
            treatmentPlan = case.treatmentPlan,
            status = case.status,
            updatedAt = LocalDateTime.now()
        )
        return caseRepository.save(updated)
    }

    @Transactional
    fun deleteById(id: Long) {
        if (!caseRepository.existsById(id)) throw CaseNotFoundException(id)
        caseRepository.deleteById(id)
    }

    @Transactional
    fun updateStatus(id: Long, status: CaseStatus): Case {
        val e = findById(id)
        return caseRepository.save(e.copy(status = status, updatedAt = LocalDateTime.now()))
    }
}
