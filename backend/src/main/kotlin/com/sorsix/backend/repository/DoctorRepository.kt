package com.sorsix.backend.repository

import com.sorsix.backend.domain.Doctor
import org.springframework.data.jpa.repository.JpaRepository

interface DoctorRepository : JpaRepository<Doctor, Long>