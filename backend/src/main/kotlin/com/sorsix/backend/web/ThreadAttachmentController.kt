package com.sorsix.backend.web

import com.sorsix.backend.domain.toResponse
import com.sorsix.backend.dto.AttachmentRequest
import com.sorsix.backend.dto.AttachmentResponse
import com.sorsix.backend.service.ThreadAttachmentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/attachments")
@CrossOrigin(origins = ["http://localhost:4200"])
class ThreadAttachmentController(
    private val service: ThreadAttachmentService
) {
    @PostMapping
    fun add(@RequestBody req: AttachmentRequest): AttachmentResponse =
        service.addAttachment(req).toResponse()
}