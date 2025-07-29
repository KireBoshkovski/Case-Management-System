package com.sorsix.backend.web

import com.sorsix.backend.domain.Examination
import com.sorsix.backend.service.ExaminationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/examinations")
class ExaminationController(
    val examinationService: ExaminationService
) {
    @PostMapping("/new")
    fun createExamination(examination: Examination) =
        ResponseEntity.ok(examinationService.save(examination))
}