package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.dto.CaseResponse
import com.sorsix.backend.dto.toResponseDto
import com.sorsix.backend.service.CaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = ["http://localhost:4200"])
class CaseController(
    val caseService: CaseService
) {

    @GetMapping
    fun getAllCases(): List<CaseResponse> {
        return caseService.getAll().map { it.toResponseDto() }
    }

    @GetMapping("/private")
    fun getAllPrivateCases(): List<Case> {
        return caseService.getAllPrivate()
    }

    @GetMapping("/public")
    fun getAllPublicCases(): List<Case> {
        return caseService.getAllPublic()
    }

    @DeleteMapping("/delete/{id}")
    fun deleteCaseById(@PathVariable id: Long): ResponseEntity<Unit> {
        caseService.deleteById(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun saveCase(case: Case): ResponseEntity<Case> {
        return ResponseEntity.ok(caseService.save(case))
    }   

    @GetMapping("/{id}")
    fun findCaseById(@PathVariable id: Long): ResponseEntity<Case> {
        return ResponseEntity.ok(caseService.findById(id))
    }

    @PutMapping
    fun updateCase(case: Case): ResponseEntity<Case> {
        return ResponseEntity.ok(caseService.update(case))
    }

    @GetMapping("/patient/{id}")
    fun byPatientId(@PathVariable id: Long): List<Case> = caseService.findAllByPatientId(id)
}