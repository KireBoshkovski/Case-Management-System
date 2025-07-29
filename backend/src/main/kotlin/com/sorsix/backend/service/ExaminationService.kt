package com.sorsix.backend.service

import com.sorsix.backend.domain.Examination
import com.sorsix.backend.repository.ExaminationRepository
import org.springframework.stereotype.Service

@Service
class ExaminationService(
    val examinationRepository: ExaminationRepository
) {
    fun save(examination: Examination) = examinationRepository.save(examination)
}