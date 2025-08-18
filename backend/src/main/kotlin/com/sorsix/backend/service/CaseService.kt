package com.sorsix.backend.service

import com.sorsix.backend.domain.cases.Case
import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.domain.cases.PublicExamination
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import com.sorsix.backend.repository.PublicCaseRepository
import com.sorsix.backend.repository.PublicExaminationRepository
import com.sorsix.backend.security.CustomUserDetails
import com.sorsix.backend.service.specification.FieldFilterSpecification
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CaseService(
    val caseRepository: CaseRepository,
    val publicCaseRepository: PublicCaseRepository,
    val publicExaminationRepository: PublicExaminationRepository,
    val patientService: PatientService,
    val doctorService: DoctorService
) {
    fun findByPatientId(user: Long, q: String?, pageable: Pageable): Page<Case> {
        val specs = listOfNotNull(
            FieldFilterSpecification.filterEqualsT<Case, Long>("patient.id", user),
            FieldFilterSpecification.filterContainsText<Case>(listOf("description", "allergies", "bloodType"), q)
        )

        val spec: Specification<Case>? = specs.reduceOrNull { acc, s -> acc.and(s) }

        return caseRepository.findAll(spec, pageable)
    }

    fun findByDoctorId(user: Long, patientId: Long?, q: String?, pageable: Pageable): Page<Case> {
        val specs = listOfNotNull(
            FieldFilterSpecification.filterEqualsT<Case, Long>("patient.id", patientId),
            FieldFilterSpecification.filterEqualsT<Case, Long>("doctor.id", user),
            FieldFilterSpecification.filterContainsText(listOf("description", "allergies", "bloodType"), q)
        )

        val spec: Specification<Case>? = specs.reduceOrNull { acc, s -> acc.and(s) }

        return caseRepository.findAll(spec, pageable)
    }

    fun findAllPublic(q: String?, pageable: Pageable): Page<PublicCase> {

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

    fun findById(id: Long): Case =
        caseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    fun findPublicById(id: Long): PublicCase =
        publicCaseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    fun findByIdSecured(id: Long, currentUser: CustomUserDetails): Case {
        val case = findById(id)
        val canAccess = when (currentUser.getRole()) {
            UserRole.PATIENT -> case.patient.id == currentUser.getId()
            UserRole.DOCTOR -> case.doctor.id == currentUser.getId()
            else -> false
        }
        if (!canAccess) throw AccessDeniedException("You are not authorized to view this case")
        return case
    }

    @Transactional
    fun save(case: CaseDto) =
        caseRepository.save(
            Case(
                id = 0,
                bloodType = case.bloodType,
                allergies = case.allergies,
                description = case.description,
                treatmentPlan = case.treatmentPlan,
                status = case.status,
                patient = patientService.findById(case.patientId),
                doctor = doctorService.findById(case.doctorId),
            )
        )

    @Transactional
    fun update(id: Long, case: CaseDto): Case {
        val existing = findById(id)

        val updated = existing.copy(
            bloodType = case.bloodType,
            allergies = case.allergies,
            description = case.description,
            treatmentPlan = case.treatmentPlan,
            status = case.status
        )
        return caseRepository.save(updated)
    }

    /*
    * Function should take a case object after the doctor approved changes from the frontend page.
    * Meaning this function should persist the new case.
    * The way it's persisting
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