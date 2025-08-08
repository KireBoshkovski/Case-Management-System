package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

enum class Visibility { ALL, PUBLIC, PRIVATE }

@Service
class CaseService(
    val caseRepository: CaseRepository
) {
    fun getCases(
        visibility: Visibility,
        patientId: Long?,
        pageable: Pageable
    ): Page<Case> {
        if (patientId != null) {
            return caseRepository.findAllByPatientId(patientId, pageable)
        }
        return when (visibility) {
            Visibility.PUBLIC -> caseRepository.findAllByPublicTrue(pageable)
            Visibility.PRIVATE -> caseRepository.findAllByPublicFalse(pageable)
            Visibility.ALL -> caseRepository.findAll(pageable)
        }
    }


    fun findById(id: Long): Case =
        caseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    @Transactional
    fun save(case: Case): Case =
        caseRepository.save(case)

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
        val case = findById(id)
        val updated = case.copy(
            status = status,
            updatedAt = LocalDateTime.now()
        )
        return caseRepository.save(updated)
    }

}