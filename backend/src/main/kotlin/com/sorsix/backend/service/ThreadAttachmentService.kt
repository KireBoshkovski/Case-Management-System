package com.sorsix.backend.service
//
//import com.sorsix.backend.domain.ThreadAttachment
//import com.sorsix.backend.dto.AttachmentRequest
//import com.sorsix.backend.repository.ThreadAttachmentRepository
//import org.springframework.stereotype.Service
//
//@Service
//class ThreadAttachmentService(
//    private val attachmentRepository: ThreadAttachmentRepository,
//    private val threadService: MedicalThreadService
//) {
//    fun addAttachment(req: AttachmentRequest): ThreadAttachment {
//        val thread = threadService.findById(req.threadId)
//        val att = ThreadAttachment(
//            thread = thread,
//            fileName = req.fileName,
//            contentType = req.contentType,
//            url = req.url
//        )
//        return attachmentRepository.save(att)
//    }
//}
