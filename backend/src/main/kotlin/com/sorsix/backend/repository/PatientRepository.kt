package com.sorsix.backend.repository

import com.sorsix.backend.domain.users.Patient
import org.springframework.data.jpa.repository.JpaRepository

interface PatientRepository : JpaRepository<Patient, Long>