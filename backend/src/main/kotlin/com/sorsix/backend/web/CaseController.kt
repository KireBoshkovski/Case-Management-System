package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.dto.toCaseDto
import com.sorsix.backend.service.CaseService
import com.sorsix.backend.service.OpenAIService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cases")
class CaseController(
    val caseService: CaseService,
    val openAIService: OpenAIService
) {

    @GetMapping
    fun getAllCases(): List<CaseDto> {
        return caseService.findAll().map { it.toCaseDto() }
    }

    @GetMapping("/private")
    fun getAllPrivateCases(): List<Case> {
        return caseService.findAllPrivate()
    }

    @GetMapping("/public")
    fun getAllPublicCases(): List<Case> {
        return caseService.findAllPublic()
    }

    @DeleteMapping("/delete/{id}")
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

    @GetMapping("/censor/{id}")
    fun censor(@PathVariable id: Long): ResponseEntity<Case> = ResponseEntity.ok(openAIService.censor(id))

    @PostMapping("/publish")
    fun publishCase(@RequestBody case: Case): ResponseEntity<String> {
        this.caseService.publishCase(case)
        return ResponseEntity.ok().build()
    }
}   