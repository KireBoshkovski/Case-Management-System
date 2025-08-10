package com.sorsix.backend.web

import com.sorsix.backend.domain.PublicCase
import com.sorsix.backend.service.CaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public")
class PublicController(
    val caseService: CaseService
) {

    /*
    * This returns all public cases in new PublicCase entity format
    *  */
    @GetMapping
    fun getAllPublicCases(): List<PublicCase> = caseService.findAllPublic()


    @GetMapping("/{id}")
    fun getPublicCaseById(@PathVariable id: Long): ResponseEntity<PublicCase> =
        ResponseEntity.ok(caseService.findPublicById(id))
}