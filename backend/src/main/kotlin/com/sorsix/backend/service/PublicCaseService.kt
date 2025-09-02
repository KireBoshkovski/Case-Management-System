package com.sorsix.backend.service

import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.domain.cases.PublicExamination
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.PublicCaseRepository
import com.sorsix.backend.repository.PublicExaminationRepository
import com.sorsix.backend.service.specification.FieldFilterSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
data class PublicCaseService(
    val publicCaseRepository: PublicCaseRepository,
    val publicExaminationRepository: PublicExaminationRepository,
) {
    fun findById(id: Long) = publicCaseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    fun findAll(q: String?, pageable: Pageable): Page<PublicCase> {
        val specs = listOfNotNull(
            FieldFilterSpecification.filterContainsText<PublicCase>(
                listOf(
                    "description", "allergies", "bloodType", "treatmentPlan",
                    "patientAgeRange", "patientGender"
                ), q
            )

        )
        val spec: Specification<PublicCase>? = specs.reduceOrNull { acc, s -> acc.and(s) }

        return publicCaseRepository.findAll(spec, pageable)
    }

    fun getPopularCases(): List<PublicCase> {
        return publicCaseRepository.findAllOrderByViewsDesc()
    }

    fun incrementViewsCount(caseId: Long) {
        publicCaseRepository.incrementViewsCount(caseId)
    }

    /*
    * Function should take a case object after the doctor approved changes from the frontend page.
    * Meaning this function should persist the new case.
    * The way it's persisting
    * */
    @Transactional
    fun publishCase(case: PublicCase): PublicCase {
        val savedCase = publicCaseRepository.save(case)

        if (case.examinations.isNotEmpty()) {
            val publicExaminations = case.examinations.map { exam ->
                PublicExamination(
                    id = null,
                    originalExaminationId = exam.originalExaminationId,
                    examinationType = exam.examinationType,
                    findings = exam.findings,
                    results = exam.results,
                    notes = exam.notes,
                    vitalSigns = if (exam.vitalSigns.isNullOrBlank()) "{}" else exam.vitalSigns,
                    examinationDate = exam.examinationDate,
                    examiningDoctorSpecialty = exam.examiningDoctorSpecialty,
                    publishedAt = LocalDateTime.now()
                )
            }
            publicExaminationRepository.saveAll(publicExaminations)

            savedCase.examinations.addAll(publicExaminations.toMutableList())
        }
        return publicCaseRepository.save(savedCase)
    }
}
