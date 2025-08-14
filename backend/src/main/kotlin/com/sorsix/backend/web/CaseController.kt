package com.sorsix.backend.web

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.PublicCase
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.dto.CaseDto
import com.sorsix.backend.dto.PageResponse
import com.sorsix.backend.dto.toCaseDto
import com.sorsix.backend.dto.toPageResponse
import com.sorsix.backend.security.CustomUserDetails
import com.sorsix.backend.service.CaseService
import com.sorsix.backend.service.OpenAIService
import com.sorsix.backend.service.Visibility
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = ["http://localhost:4200"])
class CaseController(
    private val caseService: CaseService,
    private val openAIService: OpenAIService
) {
    /**
     * Return cases available for the specific doctor/patient (PRIVATE only).
     */
    @GetMapping
    fun getAllCases(@AuthenticationPrincipal userDetails: CustomUserDetails): List<CaseDto> =
        when (userDetails.getRole()) {
            UserRole.PATIENT -> caseService.findByPatientId(userDetails.getId())
            UserRole.DOCTOR  -> caseService.findByDoctorId(userDetails.getId())
            else             -> emptyList()
        }.map { it.toCaseDto() }

    /**
     * Paginated/searchable endpoint (PUBLIC/PRIVATE/ALL).
     */
    @GetMapping("/search")
    fun getCases(
        @RequestParam(defaultValue = "ALL") visibility: Visibility,
        @RequestParam(required = false) patientId: Long?,
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<PageResponse<CaseDto>> {
        val page = caseService.getCases(visibility, patientId, q, pageable).map { it.toCaseDto() }
        return ResponseEntity.ok(page.toPageResponse())
    }

    /** Unsecured raw fetch by id  */
    @GetMapping("/{id}")
    fun findCaseById(@PathVariable id: Long): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.findById(id))

    /** Secured fetch by id */
    @GetMapping("/{id}/secured")
    fun findCaseByIdSecured(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<Case> =
        ResponseEntity.ok(caseService.findByIdSecured(id, userDetails))

    @PostMapping
    fun createCase(@RequestBody case: CaseDto): ResponseEntity<CaseDto> =
        ResponseEntity.ok(caseService.save(case).toCaseDto())

    @PutMapping("/{id}")
    fun updateCase(@PathVariable id: Long, @RequestBody case: CaseDto): ResponseEntity<CaseDto> =
        ResponseEntity.ok(caseService.update(id, case).toCaseDto())

    @DeleteMapping("/{id}")
    fun deleteCaseById(@PathVariable id: Long): ResponseEntity<Unit> {
        caseService.deleteById(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/censor/{id}")
    fun censor(@PathVariable id: Long): ResponseEntity<Case> =
        ResponseEntity.ok(openAIService.censor(id))

    @PostMapping("/publish/{id}")
    fun publishCase(@PathVariable id: Long, @RequestBody case: PublicCase): ResponseEntity<Void> {
        caseService.publishCase(case)
        return ResponseEntity.ok().build()
    }
}
