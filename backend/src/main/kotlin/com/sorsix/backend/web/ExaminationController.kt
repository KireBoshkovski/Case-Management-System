package com.sorsix.backend.web

import com.sorsix.backend.service.ExaminationService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/examinations")
class ExaminationController(
    val examinationService: ExaminationService
) {

    //TODO
}