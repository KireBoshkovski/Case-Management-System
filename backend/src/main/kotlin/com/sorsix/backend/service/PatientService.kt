package com.sorsix.backend.service

import com.sorsix.backend.repository.PatientRepository
import org.springframework.stereotype.Service

@Service
class PatientService(
    val patientRepository: PatientRepository
)