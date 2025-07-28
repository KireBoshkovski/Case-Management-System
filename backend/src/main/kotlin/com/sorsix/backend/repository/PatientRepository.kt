package com.sorsix.backend.repository

import com.sorsix.backend.domain.Patient
import org.springframework.data.jpa.repository.JpaRepository

interface PatientRepository : JpaRepository<Patient, Long>