package com.sorsix.backend.web

import com.sorsix.backend.domain.users.Patient
import com.sorsix.backend.dto.PatientDto
import com.sorsix.backend.exceptions.PatientNotFoundException
import com.sorsix.backend.service.PatientService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/patients")
data class PatientController(
    val patientService: PatientService,
) {
    @GetMapping
    fun findAll(): List<PatientDto> = patientService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Patient> =
        ResponseEntity.ok(patientService.findById(id).orElseThrow {
            PatientNotFoundException(id)
        })
}
