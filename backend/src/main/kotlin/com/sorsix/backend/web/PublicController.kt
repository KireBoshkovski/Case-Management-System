package com.sorsix.backend.web

import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.service.CaseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public")
class PublicController(
    val caseService: CaseService
) {

    /*
    * This returns all public cases in new PublicCase entity format
    *  */
    @GetMapping
    fun getAllPublicCases(
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): Page<PublicCase> = caseService.findAllPublic(q, pageable)

    @GetMapping("/{id}")
    fun getPublicCaseById(@PathVariable id: Long): ResponseEntity<PublicCase> =
        ResponseEntity.ok(caseService.findPublicById(id))
}