//package com.sorsix.backend.web
//
//import com.sorsix.backend.domain.Examination
//import com.sorsix.backend.service.ExaminationService
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/api/examinations")
//class ExaminationController(
//    val examinationService: ExaminationService
//) {
//
//    @GetMapping
//    fun getAllExaminations(): ResponseEntity<List<ExaminationDto>> {
//        val examinations = examinationService.findAll()
//        return ResponseEntity.ok(examinations)
//    }
//
//    @GetMapping("/{id}")
//    fun getExaminationById(@PathVariable id: Long): ResponseEntity<ExaminationDto> {
//        val examination = examinationService.findById(id)
//        return ResponseEntity.ok(examination)
//    }
//
//    @PostMapping
//    fun createExamination(
//        @RequestBody request: CreateExaminationRequest
//    ): ResponseEntity<ExaminationDto> {
//        val examination = examinationService.create(request)
//        return ResponseEntity.status(HttpStatus.CREATED).body(examination)
//    }
//
//}