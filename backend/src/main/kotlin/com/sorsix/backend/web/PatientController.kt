package com.sorsix.backend.web

import com.sorsix.backend.dto.PatientDto
import com.sorsix.backend.service.PatientService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = ["http://localhost:4200"])
class PatientController(
    val patientService: PatientService,
) {
    @GetMapping
    fun findAll(): ResponseEntity<List<PatientDto>> =
        ResponseEntity.ok(patientService.findAll())

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<PatientDto> =
        ResponseEntity.ok(patientService.findById(id))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        patientService.deleteById(id)
        return ResponseEntity.ok().build()
    }
}
