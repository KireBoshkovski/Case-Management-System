package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.dto.PageResponse
import com.sorsix.backend.dto.toCaseDto
import com.sorsix.backend.dto.toPageResponse
import com.sorsix.backend.service.CaseService
import com.sorsix.backend.service.Visibility
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = ["http://localhost:4200"])
class CaseController(
    val caseService: CaseService
) {
    @GetMapping
    fun getCases(
        @RequestParam(defaultValue = "ALL") visibility: Visibility,
        @RequestParam(required = false) patientId: Long?,
        @PageableDefault(size = 20, sort = ["createdAt"]) pageable: Pageable
    ): ResponseEntity<PageResponse<CaseDto>> {
        val page = caseService.getCases(visibility, patientId, pageable).map { it.toCaseDto() }
        return ResponseEntity.ok(page.toPageResponse())
    }

    @GetMapping("/{id}")
    fun findCaseById(@PathVariable id: Long): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.findById(id))

    @DeleteMapping("/{id}")
    fun deleteCaseById(@PathVariable id: Long): ResponseEntity<Unit> {
        caseService.deleteById(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun createCase(@RequestBody case: Case): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.save(case))


    @PutMapping("/{id}")
    fun updateCase(@PathVariable id: Long, @RequestBody case: Case): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.update(case))

}