package com.sorsix.backend.service

import com.sorsix.backend.domain.Fork
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.dto.ForkDto
import com.sorsix.backend.exceptions.CaseNotFoundException
import com.sorsix.backend.exceptions.DoctorNotFoundException
import com.sorsix.backend.exceptions.ForkNotFoundException
import com.sorsix.backend.repository.DoctorRepository
import com.sorsix.backend.repository.ForkRepository
import com.sorsix.backend.repository.PublicCaseRepository
import com.sorsix.backend.security.CustomUserDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class ForkService(
    val forkRepository: ForkRepository,
    val publicCaseRepository: PublicCaseRepository,
    val doctorRepository: DoctorRepository,

    ) {
    fun findAll(): List<Fork> = forkRepository.findAll()

    fun findById(id: Long) = forkRepository.findById(id)

    fun deleteById(id: Long) = forkRepository.deleteById(id)

    fun createFork(fork: ForkDto, userDetails: CustomUserDetails): Fork {
        val case = publicCaseRepository.findByIdOrNull(fork.originId) ?: throw CaseNotFoundException(fork.originId)

        val doctor = if (userDetails.getRole() == UserRole.DOCTOR) {
            doctorRepository.findByIdOrNull(userDetails.getId())
                ?: throw DoctorNotFoundException(userDetails.getId())
        } else {
            throw AccessDeniedException("You are not authorized to create a fork")
        }

        return forkRepository.save(
            Fork(
                title = fork.title,
                description = fork.description,
                alternativeDiagnosis = fork.alternativeDiagnosis,
                alternativeTreatment = fork.alternativeTreatment,
                analysisNotes = fork.analysisNotes,
                recommendations = fork.recommendations,
                origin = case,
                editor = doctor
            )
        )
    }

    fun update(id: Long, fork: ForkDto): Fork {
        val existingFork = forkRepository.findById(id).orElseThrow { ForkNotFoundException(id) }

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

    fun findAllByOriginId(id: Long): List<Fork> = forkRepository.findAllByOriginId(id)
}