package com.sorsix.backend.service

import com.sorsix.backend.repository.DoctorRepository
import org.springframework.stereotype.Service

@Service
class DoctorService(
    val doctorRepository: DoctorRepository
)