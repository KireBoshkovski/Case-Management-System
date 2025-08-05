package com.sorsix.backend.service

import com.sorsix.backend.domain.ThreadDiscussion
import com.sorsix.backend.exceptions.MedicalThreadNotFoundException
import com.sorsix.backend.repository.ThreadDiscussionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ThreadDiscussionService(
    val threadService: MedicalThreadService,
    val discussionRepository: ThreadDiscussionRepository,
    val doctorService: DoctorService
) {

    fun findById(id: Long): ThreadDiscussion =
        discussionRepository.findByIdOrNull(id) ?: throw MedicalThreadNotFoundException(id)

    fun findByThreadId(threadId: Long): List<ThreadDiscussion> =
        discussionRepository.findByThreadIdOrderByCreatedAtAsc(threadId)


    fun addDiscussion(
        threadId: Long,
        doctorId: Long,
        content: String,
        diagnosisSuggestion: String?,
        confidenceLevel: Int?,
        parentDiscussionId: Long? = null
    ): ThreadDiscussion {
        val thread = threadService.findById(threadId)
        val doctor = doctorService.findById(doctorId)
        val parentDiscussion = parentDiscussionId?.let { findById(it) }

        val discussion = ThreadDiscussion(
            content = content,
            diagnosisSuggestion = diagnosisSuggestion,
            confidenceLevel = confidenceLevel,
            thread = thread,
            doctor = doctor,
            parentDiscussion = parentDiscussion
        )

        return discussionRepository.save(discussion)
    }
}