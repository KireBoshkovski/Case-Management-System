package com.sorsix.backend.service

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.domain.cases.Case
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import com.sorsix.backend.service.specification.FieldFilterSpecification
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class CaseService(
    val caseRepository: CaseRepository,
    val patientService: PatientService,
    val doctorService: DoctorService
) {
    private val logger = LoggerFactory.getLogger(CaseService::class.java)

    fun findByPatientId(user: Long, q: String?, pageable: Pageable): Page<Case> {
        logger.debug("Fetching cases for patient with ID: [{}], query={}", user, q)
        val specs = listOfNotNull(
            FieldFilterSpecification.filterEqualsT<Case, Long>("patient.id", user),
            FieldFilterSpecification.filterContainsText<Case>(listOf("description", "allergies", "bloodType"), q)
        )
        val spec: Specification<Case>? = specs.reduceOrNull { acc, s -> acc.and(s) }
        return caseRepository.findAll(spec, pageable)
    }

    fun findByDoctorId(user: Long, patientId: Long?, q: String?, pageable: Pageable): Page<Case> {
        logger.debug("Fetching cases for doctor with ID: [{}], patient ID: [{}], query={}", user, patientId, q)
        val specs = listOfNotNull(
            FieldFilterSpecification.filterEqualsT<Case, Long>("patient.id", patientId),
            FieldFilterSpecification.filterEqualsT<Case, Long>("doctor.id", user),
            FieldFilterSpecification.filterContainsText(listOf("description", "allergies", "bloodType"), q)
        )
        val spec: Specification<Case>? = specs.reduceOrNull { acc, s -> acc.and(s) }
        return caseRepository.findAll(spec, pageable)
    }

    fun findById(id: Long): Case {
        logger.debug("Fetching case by ID: [{}]", id)
        return caseRepository.findByIdOrNull(id) ?: throw CaseNotFoundException(id)
    }

    fun findByIdSecured(id: Long, currentUser: CustomUserDetails): Case {
        logger.debug(
            "Fetching case by ID: [{}] with security check for user with ID: [{}] role [{}]",
            id,
            currentUser.getId(),
            currentUser.getRole()
        )
        val case = findById(id)
        val canAccess = when (currentUser.getRole()) {
            UserRole.PATIENT -> case.patient.id == currentUser.getId()
            UserRole.DOCTOR -> case.doctor.id == currentUser.getId()
            else -> false
        }
        if (!canAccess) {
            logger.warn("Access denied for user with ID: [{}] to case with ID: [{}]", currentUser.getId(), id)
            throw AccessDeniedException("You are not authorized to view this case")
        }
        return case
    }

    @Transactional
    fun save(case: CaseDto, doctorId: Long): Case {
        logger.info(
            "Saving new case for patient with ID: [{}] from doctor with ID: [{}]",
            case.patientId,
            doctorId
        )
        val saved = caseRepository.save(
            Case(
                id = null,
                bloodType = case.bloodType,
                allergies = case.allergies,
                description = case.description,
                treatmentPlan = case.treatmentPlan,
                status = case.status,
                patient = patientService.findById(case.patientId),
                doctor = doctorService.findById(doctorId),
            )
        )
        logger.debug("Saved case with ID: [{}]", saved.id)
        return saved
    }

    @Transactional
    fun update(id: Long, case: CaseDto): Case {
        logger.info("Updating case ID: [{}]", id)
        val existing = findById(id)

        val updated = existing.copy(
            bloodType = case.bloodType,
            allergies = case.allergies,
            description = case.description,
            treatmentPlan = case.treatmentPlan,
            status = case.status
        )
        val saved = caseRepository.save(updated)
        logger.debug("Updated case with ID: [{}] successfully", saved.id)
        return saved
    }
}