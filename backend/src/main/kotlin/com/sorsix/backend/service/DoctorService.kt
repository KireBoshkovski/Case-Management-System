package com.sorsix.backend.service

import com.sorsix.backend.domain.Doctor
import com.sorsix.backend.repository.DoctorRepository
import org.springframework.stereotype.Service

@Service
class DoctorService(
    val doctorRepository: DoctorRepository
) {
    fun findAll() = doctorRepository.findAll()
    fun findById(id: Long) = doctorRepository.findById(id).orElseThrow { IllegalArgumentException("Doctor with id $id not found") }//TODO
    fun save(doctor: Doctor) = doctorRepository.save(doctor)
    fun deleteById(id: Long) = doctorRepository.deleteById(id)
}