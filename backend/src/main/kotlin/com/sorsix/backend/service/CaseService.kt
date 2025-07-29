package com.sorsix.backend.service

import com.sorsix.backend.domain.Case
import com.sorsix.backend.repository.CaseRepository
import org.springframework.stereotype.Service

@Service
class CaseService(
    val caseRepository: CaseRepository
) {
    fun getAll() = caseRepository.findAll()

    fun getAllPrivate() = caseRepository.findAll() //TODO

    fun getAllPublic() = caseRepository.findAll() //TODO

    fun getById(id: Long) = caseRepository.findById(id)

    fun save(case: Case) = caseRepository.save(case)

    fun update(case: Case) = caseRepository.save(case)

    fun deleteById(id: Long) = caseRepository.deleteById(id)

}