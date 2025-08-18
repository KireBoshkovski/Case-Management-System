package com.sorsix.backend.repository

import com.sorsix.backend.domain.users.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PatientRepository : JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient>