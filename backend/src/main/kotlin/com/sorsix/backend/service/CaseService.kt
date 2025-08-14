package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.PublicCase
import com.sorsix.backend.domain.PublicExamination
import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import com.sorsix.backend.repository.PublicCaseRepository
import com.sorsix.backend.repository.PublicExaminationRepository
import com.sorsix.backend.security.CustomUserDetails
import com.sorsix.backend.specification.CaseSpecifications
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

enum class Visibility { PUBLIC, PRIVATE, ALL }

@Service
class CaseService(
    private val caseRepository: CaseRepository,
    private val publicCaseRepository: PublicCaseRepository,
    private val publicExaminationRepository: PublicExaminationRepository,
    private val patientService: PatientService,
    private val doctorService: DoctorService
) {

    @Transactional
    fun findByPatientId(id: Long): List<Case> =
        caseRepository.findAllByPatientId(id)

    @Transactional
    fun findByDoctorId(id: Long): List<Case> =
        caseRepository.findAllByDoctorId(id)

    @Transactional
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

        val specs = listOfNotNull(
            CaseSpecifications.hasPatientId(patientId),
            CaseSpecifications.isPublic(publicVal),
            CaseSpecifications.searchByTermOrId(q?.trim())
        )

        val spec: Specification<Case>? = specs.reduceOrNull { acc, s -> acc.and(s) }

        return if (spec != null) caseRepository.findAll(spec, pageable)
        else caseRepository.findAll(pageable)
    }

    @Transactional
    fun findById(id: Long): Case =
        caseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    @Transactional
    fun findPublicById(id: Long): PublicCase =
        publicCaseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)

    @Transactional
    fun findByIdSecured(id: Long, currentUser: CustomUserDetails): Case {
        val c = findById(id)
        val canAccess = when (currentUser.getRole()) {
            UserRole.PATIENT -> c.patient.id == currentUser.getId()
            UserRole.DOCTOR  -> c.doctor.id == currentUser.getId()
            else -> false
        }
        if (!canAccess) throw AccessDeniedException("You are not authorized to view this case")
        return c
    }

    @Transactional
    fun save(dto: CaseDto): Case =
        caseRepository.save(
            Case(
                id = null, // important for @GeneratedValue
                bloodType = dto.bloodType,
                allergies = dto.allergies,
                description = dto.description,
                treatmentPlan = dto.treatmentPlan,
                status = dto.status,
                patient = patientService.findById(dto.patientId),
                doctor = doctorService.findById(dto.doctorId),
            )
        )

    @Transactional
    fun update(id: Long, dto: CaseDto): Case {
        val existing = findById(id)
        val updated = existing.copy(
            bloodType = dto.bloodType,
            allergies = dto.allergies,
            description = dto.description,
            treatmentPlan = dto.treatmentPlan,
            status = dto.status,
            updatedAt = LocalDateTime.now()
        )
        return caseRepository.save(updated)
    }

    @Transactional
    fun deleteById(id: Long) {
        if (!caseRepository.existsById(id)) throw CaseNotFoundException(id)
        caseRepository.deleteById(id)
    }

    /**
     * Persist a public case and its public examinations.
     */
    @Transactional
    fun publishCase(case: PublicCase) {
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

    @Transactional
    fun updateStatus(id: Long, status: CaseStatus): Case {
        val e = findById(id)
        return caseRepository.save(e.copy(status = status, updatedAt = LocalDateTime.now()))
    }
}
