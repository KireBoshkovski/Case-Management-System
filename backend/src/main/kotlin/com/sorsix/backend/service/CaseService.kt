package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CaseService(
    val caseRepository: CaseRepository
) {
    fun getAll() = caseRepository.findAllWithDetails()

    fun getAllPrivate() = caseRepository.findAllByPublicFalse()

    fun getAllPublic() = caseRepository.findAllByPublicTrue()

    fun save(case: Case) = caseRepository.save(case)

    fun deleteById(id: Long) = caseRepository.deleteById(id)

    fun findById(id: Long): Case {
        return caseRepository.findById(id).orElseThrow {
            CaseNotFoundException(id)
        }
    }

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

    fun findAllByPatientId(patientId: Long) = caseRepository.findAllByPatientId(patientId)
}