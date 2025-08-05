package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.dto.toCaseDto
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
    fun getAllCases(): ResponseEntity<List<CaseDto>> =
        ResponseEntity.ok(caseService.findAll().map { it.toCaseDto() })


    @GetMapping("/public")
    fun getAllPublicCases(): ResponseEntity<List<CaseDto>> =
        ResponseEntity.ok(caseService.findAllPublic().map { it.toCaseDto() })


    @GetMapping("/private")
    fun getAllPrivateCases(): ResponseEntity<List<CaseDto>> =
        ResponseEntity.ok(caseService.findAllPrivate().map { it.toCaseDto() })


    @GetMapping("/patient/{id}")
    fun findAllCasesByPatientId(@PathVariable id: Long): ResponseEntity<List<CaseDto>> =
        ResponseEntity.ok(caseService.findAllByPatientId(id).map { it.toCaseDto() })


    @DeleteMapping("/{id}")
    fun deleteCaseById(@PathVariable id: Long): ResponseEntity<Unit> {
        caseService.deleteById(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}")
    fun findCaseById(@PathVariable id: Long): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.findById(id))


    @PostMapping
    fun createCase(@RequestBody case: Case): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.save(case))


    @PutMapping("/{id}")
    fun updateCase(@PathVariable id: Long, @RequestBody case: Case): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.update(case))

}