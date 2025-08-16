package com.sorsix.backend.web

import com.sorsix.backend.service.ExaminationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/examinations")
class ExaminationController(
    val examinationService: ExaminationService
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = examinationService.findById(id)
}