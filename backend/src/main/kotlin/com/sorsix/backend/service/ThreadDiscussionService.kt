package com.sorsix.backend.service

import com.sorsix.backend.domain.ThreadDiscussion
import com.sorsix.backend.dto.CreateDiscussionRequest
import com.sorsix.backend.exceptions.MedicalThreadNotFoundException
import com.sorsix.backend.repository.ThreadDiscussionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ThreadDiscussionService(
    private val threadService: MedicalThreadService,
    private val discussionRepository: ThreadDiscussionRepository,
    private val doctorService: DoctorService
) {
    fun listByThread(threadId: Long, pageable: Pageable): Page<ThreadDiscussion> =
        discussionRepository.findByThreadIdOrderByCreatedAtAsc(threadId, pageable)

    fun findById(id: Long): ThreadDiscussion =
        discussionRepository.findByIdOrNull(id) ?: throw MedicalThreadNotFoundException(id)

    fun addDiscussion(req: CreateDiscussionRequest): ThreadDiscussion {
        val thread = threadService.findById(req.threadId)
        val doctor = doctorService.findById(req.doctorId)
        val parent = req.parentDiscussionId?.let { findById(it) }

        val entity = ThreadDiscussion(
            content = req.content,
            diagnosisSuggestion = req.diagnosisSuggestion,
            confidenceLevel = req.confidenceLevel,
            thread = thread,
            doctor = doctor,
            parentDiscussion = parent
        )
        return discussionRepository.save(entity)
    }
}
