package com.sorsix.backend.web

import com.sorsix.backend.domain.users.Patient
import com.sorsix.backend.dto.PatientDto
import com.sorsix.backend.service.PatientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/patients")
data class PatientController(
    val patientService: PatientService,
) {
    @GetMapping
    fun findAll(
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["lastName"], direction = Sort.Direction.DESC) pageable: Pageable
    ): Page<PatientDto> = patientService.findAll(q, pageable)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Patient> =
        ResponseEntity.ok(patientService.findById(id))
}
