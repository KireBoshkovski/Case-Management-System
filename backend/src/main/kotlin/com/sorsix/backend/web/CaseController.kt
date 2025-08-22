package com.sorsix.backend.web

import com.sorsix.backend.domain.cases.Case
import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.dto.toCaseDto
import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.service.CaseService
import com.sorsix.backend.service.OpenAIService
import com.sorsix.backend.service.PublicCaseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cases")
class CaseController(
    val caseService: CaseService,
    val publicCaseService: PublicCaseService,
    val openAIService: OpenAIService
) {
    /**
     * Return cases available for the specific doctor/user logged in.
     * All cases returned from this function are PRIVATE.
     * Also, pagination and searching is through this endpoint
     * */
    @GetMapping
    fun findCases(
        @RequestParam(required = false) patientId: Long?,
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): Page<CaseDto> {
        val cases = when (userDetails.getRole()) {
            UserRole.PATIENT -> caseService.findByPatientId(userDetails.getId(), q, pageable)
            UserRole.DOCTOR -> caseService.findByDoctorId(userDetails.getId(), patientId, q, pageable)
            else -> Page.empty(pageable)
        }
        return cases.map { it.toCaseDto() }
    }

    /**
     * Returns case by case id but first checks if user actually have access to the case.
     * Users who have access to case: Patients which belong to the case, Doctors that created the case.
     * */
    @GetMapping("/{id}")
    fun findCaseById(
        @PathVariable id: Long, @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<Case> = ResponseEntity.ok(caseService.findByIdSecured(id, userDetails))

    @PostMapping
    fun createCase(@RequestBody case: CaseDto): ResponseEntity<CaseDto> =
        ResponseEntity.ok(caseService.save(case).toCaseDto())

    @PutMapping("/{id}")
    fun updateCase(@PathVariable id: Long, @RequestBody case: CaseDto): ResponseEntity<CaseDto> =
        ResponseEntity.ok(caseService.update(id, case).toCaseDto())

    @GetMapping("/censor/{id}")
    fun censor(@PathVariable id: Long): ResponseEntity<Case> = ResponseEntity.ok(openAIService.censor(id))

    @PostMapping("/publish/{id}")
    fun publishCase(@PathVariable id: Long, @RequestBody case: PublicCase): ResponseEntity<Void> {
        this.publicCaseService.publishCase(case)
        return ResponseEntity.ok().build()
    }
}   