package com.sorsix.backend.web

import com.sorsix.backend.domain.toResponse
import com.sorsix.backend.dto.CreateThreadRequest
import com.sorsix.backend.dto.ThreadResponse
import com.sorsix.backend.service.MedicalThreadService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/threads")
@CrossOrigin(origins = ["http://localhost:4200"])
class MedicalThreadController(
    private val service: MedicalThreadService
) {
    @GetMapping("/active")
    fun getActive(pageable: Pageable): Page<ThreadResponse> =
        service.getActive(pageable).map { it.toResponse() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ThreadResponse =
        service.findById(id).toResponse()

    @GetMapping("/{id}/forks")
    fun getForks(@PathVariable id: Long, pageable: Pageable): Page<ThreadResponse> =
        service.getForksOf(id, pageable).map { it.toResponse() }

    @PostMapping
    fun create(@RequestBody req: CreateThreadRequest): ThreadResponse =
        service.createThread(req).toResponse()

    @PostMapping("/{id}/fork")
    fun fork(@PathVariable id: Long, @RequestParam forkingDoctorId: Long): ThreadResponse =
        service.forkThread(id, forkingDoctorId).toResponse()

    @PostMapping("/{id}/close")
    fun close(@PathVariable id: Long): ThreadResponse =
        service.closeThread(id).toResponse()
}
