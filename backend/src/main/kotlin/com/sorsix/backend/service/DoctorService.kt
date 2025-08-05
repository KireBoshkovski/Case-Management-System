package com.sorsix.backend.service

import com.sorsix.backend.domain.users.Doctor
import com.sorsix.backend.repository.DoctorRepository
import org.springframework.stereotype.Service

@Service
class DoctorService(
    val doctorRepository: DoctorRepository
) {
    fun getAll() = doctorRepository.findAll()
    fun getById(id: Long) = doctorRepository.findById(id)
    fun save(doctor: Doctor) = doctorRepository.save(doctor)
    fun deleteById(id: Long) = doctorRepository.deleteById(id)
}