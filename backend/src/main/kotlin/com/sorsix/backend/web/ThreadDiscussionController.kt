package com.sorsix.backend.web

import com.sorsix.backend.domain.toResponse
import com.sorsix.backend.dto.CreateDiscussionRequest
import com.sorsix.backend.dto.DiscussionResponse
import com.sorsix.backend.service.ThreadDiscussionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/discussions")
@CrossOrigin(origins = ["http://localhost:4200"])
class ThreadDiscussionController(
    private val service: ThreadDiscussionService
) {
    @GetMapping("/thread/{threadId}")
    fun listByThread(@PathVariable threadId: Long, pageable: Pageable): Page<DiscussionResponse> =
        service.listByThread(threadId, pageable).map { it.toResponse() }

    @PostMapping
    fun add(@RequestBody req: CreateDiscussionRequest): DiscussionResponse =
        service.addDiscussion(req).toResponse()
}
