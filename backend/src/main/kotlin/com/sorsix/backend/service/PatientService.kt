package com.sorsix.backend.service

import com.sorsix.backend.domain.Patient
import com.sorsix.backend.repository.PatientRepository
import org.springframework.stereotype.Service

@Service
class PatientService(
    val patientRepository: PatientRepository
){
    fun getAll() = patientRepository.findAll()
    fun getById(id: Long) = patientRepository.findById(id)
    fun save(patient: Patient) = patientRepository.save(patient)
    fun deleteById(id: Long) = patientRepository.deleteById(id)
}