package com.sorsix.backend.web

import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.service.PublicCaseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public")
class PublicController(
    val publicCaseService: PublicCaseService
) {

    /*
    * This returns all public cases in new PublicCase entity format
    *  */
    @GetMapping
    fun getAllPublicCases(
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): Page<PublicCase> = publicCaseService.findAll(q, pageable)

    @GetMapping("/popular")
    fun getPopularCases() = publicCaseService.getPopularCases()

    @GetMapping("/{id}")
    @Transactional
    fun getPublicCaseById(@PathVariable id: Long): ResponseEntity<PublicCase> {
        publicCaseService.incrementViewsCount(id)
        return ResponseEntity.ok(publicCaseService.findById(id))
    }
}