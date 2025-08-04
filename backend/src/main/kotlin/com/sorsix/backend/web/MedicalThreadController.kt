package com.sorsix.backend.web

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.service.MedicalThreadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/threads")
@CrossOrigin(origins = ["http://localhost:4200"])
class MedicalThreadController(
    val medicalThreadService: MedicalThreadService
) {
    @PostMapping("/create")
    fun createThread(
        @RequestParam caseId: Long,
        @RequestParam creatingDoctorId: Long,
        @RequestParam title: String
    ): ResponseEntity<MedicalThread?> {
        return ResponseEntity.ok(medicalThreadService.createThreadFromCase(caseId=caseId, creatingDoctorId=creatingDoctorId, title=title))
    }//TODO DTO

//    @PostMapping("/{threadId}/fork")
//    fun forkThread()


//    @GetMapping
//    fun getActiveThreads()


}