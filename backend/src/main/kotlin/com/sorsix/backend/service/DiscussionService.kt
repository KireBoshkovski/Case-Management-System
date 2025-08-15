package com.sorsix.backend.service

import com.sorsix.backend.domain.discussions.Comment
import com.sorsix.backend.domain.discussions.Discussion
import com.sorsix.backend.dto.CommentDto
import com.sorsix.backend.dto.DiscussionDto
import com.sorsix.backend.exceptions.DiscussionNotFoundException
import com.sorsix.backend.exceptions.DoctorNotFoundException
import com.sorsix.backend.repository.CommentRepository
import com.sorsix.backend.repository.DiscussionRepository
import com.sorsix.backend.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiscussionService(
    private val discussionRepository: DiscussionRepository,
    private val userRepository: UserRepository,
    private val caseService: CaseService,
    private val commentRepository: CommentRepository
) {

    fun getDiscussionsByCase(caseId: Long): List<Discussion> {
        return discussionRepository.findAllByPublicCaseId(caseId)
    }

    fun createDiscussion(dto: DiscussionDto): Discussion {
        val user = userRepository.findByIdOrNull(dto.userId)
            ?: throw DoctorNotFoundException(dto.userId)

        val case = caseService.findPublicById(dto.caseId)

        return discussionRepository.save(
            Discussion(
                id = null,
                title = dto.title,
                description = dto.description,
                user = user,
                publicCase = case
            )
        )
    }

    fun getCommentsByDiscussion(discussionId: Long) = commentRepository.findAllByDiscussionId(discussionId)

    @Transactional
    fun addComment(dto: CommentDto): Comment {
        val user = userRepository.findByIdOrNull(dto.userId)
            ?: throw DoctorNotFoundException(dto.userId)
        val discussion = discussionRepository.findById(dto.discussionId)
            .orElseThrow { DiscussionNotFoundException(dto.discussionId) }

        val parent = dto.parentId?.let { commentRepository.findByIdOrNull(it) }

        return commentRepository.save(
            Comment(
                id = null,
                content = dto.content,
                discussion = discussion,
                user = user,
                parent = parent
            )
        )
    }

}