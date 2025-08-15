package com.sorsix.backend.service

import com.sorsix.backend.domain.cases.Examination
import com.sorsix.backend.repository.ExaminationRepository
import org.springframework.stereotype.Service

@Service
class ExaminationService(
    val examinationRepository: ExaminationRepository
) {

    fun findAll(): List<Examination> =
        examinationRepository.findAll()
    //TODO
}