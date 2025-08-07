package com.sorsix.backend.service

import com.sorsix.backend.exceptions.DoctorNotFoundException
import com.sorsix.backend.repository.DoctorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DoctorService(
    val doctorRepository: DoctorRepository
) {
    fun findAll() =
        doctorRepository.findAll()

    fun findById(id: Long) =
        doctorRepository.findByIdOrNull(id) ?: throw DoctorNotFoundException(id)

    fun deleteById(id: Long) =
        doctorRepository.deleteById(id)
}