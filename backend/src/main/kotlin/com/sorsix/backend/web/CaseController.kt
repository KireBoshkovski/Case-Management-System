package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.service.CaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cases")
class CaseController(
    val caseService: CaseService
) {

    @GetMapping
    fun getAllCases(): ResponseEntity<List<Case>> {
        return ResponseEntity.ok(caseService.getAll())
    }

    @GetMapping("/private")
    fun getAllPrivateCases(): ResponseEntity<List<Case>> {
        return ResponseEntity.ok(caseService.getAllPrivate())
    }

    @GetMapping("/public")
    fun getAllPublicCases(): ResponseEntity<List<Case>> {
        return ResponseEntity.ok(caseService.getAllPublic())
    }

    @GetMapping("/delete/{id}")
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

}