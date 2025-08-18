package com.sorsix.backend.service

import com.sorsix.backend.domain.users.Patient
import com.sorsix.backend.dto.PatientDto
import com.sorsix.backend.dto.toPatientDto
import com.sorsix.backend.exceptions.PatientNotFoundException
import com.sorsix.backend.repository.PatientRepository
import com.sorsix.backend.service.specification.FieldFilterSpecification
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PatientService(
    val patientRepository: PatientRepository
) {
    fun findAll(q: String?, pageable: Pageable): Page<PatientDto> {
        val specs = listOfNotNull(
            FieldFilterSpecification.filterContainsText<Patient>(listOf("firstName", "lastName"), q)
        )

        val spec: Specification<Patient>? = specs.reduceOrNull { acc, s -> acc.and(s) }

        return patientRepository.findAll(spec, pageable).map { it.toPatientDto() }
    }

    fun findById(id: Long): Patient =
        patientRepository.findByIdOrNull(id) ?: throw PatientNotFoundException(id)

    @Transactional
    fun save(patient: Patient) = patientRepository.save(patient)

    @Transactional
    fun update(id: Long, patientUpdate: Patient): Patient {
        val existing = patientRepository.findByIdOrNull(id) ?: throw PatientNotFoundException(id)
        val updated = existing.copy(
            firstName = patientUpdate.firstName,
            lastName = patientUpdate.lastName,
            email = patientUpdate.email,
            phoneNumber = patientUpdate.phoneNumber,
            dateOfBirth = patientUpdate.dateOfBirth,
            gender = patientUpdate.gender,
            address = patientUpdate.address,
        )
        return patientRepository.save(updated)
    }
}