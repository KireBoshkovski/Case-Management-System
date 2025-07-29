package com.sorsix.backend.service

import com.sorsix.backend.domain.Fork
import com.sorsix.backend.exceptions.ForkNotFoundException
import com.sorsix.backend.repository.ForkRepository
import org.springframework.stereotype.Service

@Service
class ForkService(
    val forkRepository: ForkRepository,
    val caseService: CaseService
) {
    fun getAll() = forkRepository.findAll()
    fun getById(id: Long) = forkRepository.findById(id)
    fun save(fork: Fork) = forkRepository.save(fork)
    fun deleteById(id: Long) = forkRepository.deleteById(id)

    fun createFork(title: String, description: String, caseId: Long): Fork {

        val case = caseService.findById(caseId)
        val doctor = case.doctor
        return forkRepository.save(Fork(title = title, description = description, origin = case, editor = doctor))
    }

    fun update(fork: Fork): Fork {
        val id = fork.id ?: throw IllegalArgumentException("Fork ID cannot be null for update")

        val existingFork = forkRepository.findById(id)
            .orElseThrow { ForkNotFoundException(id) }

        val updatedFork = existingFork.copy(
            title = fork.title,
            description = fork.description,
            alternativeDiagnosis = fork.alternativeDiagnosis,
            alternativeTreatment = fork.alternativeTreatment,
            analysisNotes = fork.analysisNotes,
            recommendations = fork.recommendations
        )

        return forkRepository.save(updatedFork)
    }


}