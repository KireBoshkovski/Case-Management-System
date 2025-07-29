package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.repository.CaseRepository
import org.springframework.stereotype.Service

@Service
class CaseService(
    val caseRepository: CaseRepository
) {
    fun getAll() = caseRepository.findAll()

    fun save(case: Case) = caseRepository.save(case)
    fun deleteById(id: Long) = caseRepository.deleteById(id)
    fun findById(id: Long): Case {
        return caseRepository.findById(id).orElseThrow {
            CaseNotFoundException(id)
        }
    }
}