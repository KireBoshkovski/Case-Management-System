package com.sorsix.backend.service

import com.sorsix.backend.repository.ExaminationRepository
import org.springframework.stereotype.Service

@Service
class ExaminationService(
    val examinationRepository: ExaminationRepository
) {
    fun getAll() = examinationRepository.findAll()
    fun getById(id: Long) = examinationRepository.findById(id)
}