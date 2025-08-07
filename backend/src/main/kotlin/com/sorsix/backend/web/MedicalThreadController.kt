package com.sorsix.backend.web

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.dto.CreateThreadRequest
import com.sorsix.backend.service.MedicalThreadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/threads")
class MedicalThreadController(
    val medicalThreadService: MedicalThreadService
) {
    @PostMapping
    fun createThread(@RequestBody request: CreateThreadRequest): ResponseEntity<MedicalThread> =
        ResponseEntity.ok(
            medicalThreadService.createThreadFromCase(
                caseId = request.caseId, creatingDoctorId = request.creatingDoctorId, title = request.title
            )
        )

    @PostMapping("/{threadId}/fork")
    fun forkThread(
        @PathVariable threadId: Long, @RequestParam forkingDoctorId: Long
    ): ResponseEntity<MedicalThread?> =
        ResponseEntity.ok(
            medicalThreadService.forkThread(
                originalThreadId = threadId,
                forkingDoctorId = forkingDoctorId,
            )
        )

    @GetMapping
    fun getActiveThreads(): ResponseEntity<List<MedicalThread>> =
        ResponseEntity.ok(medicalThreadService.findActiveThreads())

    @GetMapping("/{id}")
    fun getThreadById(@PathVariable id: Long) =
        ResponseEntity.ok(medicalThreadService.findById(id))
}