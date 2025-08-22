package com.sorsix.backend.service

import com.sorsix.backend.domain.cases.Examination
import com.sorsix.backend.exceptions.ExaminationNotFoundException
import com.sorsix.backend.repository.ExaminationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExaminationService(
    val examinationRepository: ExaminationRepository
) {

    fun findAll(): List<Examination> =
        examinationRepository.findAll()

    fun findById(id: Long): Examination =
        examinationRepository.findByIdOrNull(id) ?: throw ExaminationNotFoundException(id)
}