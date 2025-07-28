package com.sorsix.backend.service

import com.sorsix.backend.repository.ExaminationRepository
import org.springframework.stereotype.Service

@Service
class ExaminationService(
    val examinationRepository: ExaminationRepository
)