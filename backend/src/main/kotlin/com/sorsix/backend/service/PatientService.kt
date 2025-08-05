package com.sorsix.backend.service

import com.sorsix.backend.domain.Patient
import com.sorsix.backend.dto.PatientDto
import com.sorsix.backend.dto.toPatientDto
import com.sorsix.backend.exceptions.PatientNotFoundException
import com.sorsix.backend.repository.PatientRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PatientService(
    val patientRepository: PatientRepository
) {
    fun findAll(): List<PatientDto> =
        patientRepository.findAll().map { it.toPatientDto() }

    fun findById(id: Long): PatientDto? =
        patientRepository.findByIdOrNull(id)?.toPatientDto() ?: throw PatientNotFoundException(id)

    @Transactional
    fun save(patient: Patient) = patientRepository.save(patient)

    @Transactional
    fun deleteById(id: Long) {
        if (!patientRepository.existsById(id)) throw PatientNotFoundException(id)
        return patientRepository.deleteById(id)
    }

    @Transactional
    fun update(id: Long, patientUpdate: Patient): Patient {
        val existing = patientRepository.findByIdOrNull(id)?: throw PatientNotFoundException(id)
        val updated = existing.copy(
            firstName = patientUpdate.firstName,
            lastName = patientUpdate.lastName,
            email = patientUpdate.email,
            phoneNumber = patientUpdate.phoneNumber,
            address = patientUpdate.address,
            dateOfBirth = patientUpdate.dateOfBirth,
        )
        return patientRepository.save(updated)
    }

}