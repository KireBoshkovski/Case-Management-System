package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.service.CaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cases")
class CaseController(
    val caseService: CaseService
) {
    @GetMapping("/{id}")
    fun findCaseById(@PathVariable id: Long): ResponseEntity<Case> {
        return ResponseEntity.ok(caseService.findById(id))
    }
}