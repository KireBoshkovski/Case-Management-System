package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.PublicCase
import com.sorsix.backend.domain.PublicExamination
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import com.sorsix.backend.repository.PublicCaseRepository
import com.sorsix.backend.repository.PublicExaminationRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CaseService(
    val caseRepository: CaseRepository,
    val publicCaseRepository: PublicCaseRepository,
    val publicExaminationRepository: PublicExaminationRepository
) {
    fun findAll(): List<Case> =
        caseRepository.findAllWithDetails()

    fun findAllByPatientId(patientId: Long) =
        caseRepository.findAllByPatientId(patientId)

//    fun findAllPrivate(): List<Case> =
//        caseRepository.findAllByPublicFalse()

    fun findAllPublic(): List<PublicCase> =
        publicCaseRepository.findAll()

    fun findById(id: Long): Case =
        caseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    @Transactional
    fun save(case: Case): Case =
        caseRepository.save(case)

    @Transactional
    fun update(case: Case): Case {
        val existing = findById(case.id)

        val updated = existing.copy(
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

    /*
    *
    * Function should take a case object after the doctor approved changes from the frontend page.
    * Meaning this function should persist the new case.
    * The way it's persisting
    *
    * */
    @Transactional
    fun publishCase(case: PublicCase) {
        val savedCase = publicCaseRepository.save(case)

        if (case.examinations.isNotEmpty()) {
            val publicExaminations = case.examinations.map { exam ->
                PublicExamination(
                    id = 0,
                    originalExaminationId = exam.originalExaminationId,
                    examinationType = exam.examinationType,
                    findings = exam.findings,
                    results = exam.results,
                    notes = exam.notes,
                    vitalSigns = exam.vitalSigns,
                    examinationDate = exam.examinationDate,
                    examiningDoctorSpecialty = exam.examiningDoctorSpecialty,
                    publishedAt = LocalDateTime.now()
                )
            }
            publicExaminationRepository.saveAll(publicExaminations)

            savedCase.examinations.addAll(publicExaminations.toMutableList())
            publicCaseRepository.save(savedCase)
        }
    }
}