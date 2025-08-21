package com.sorsix.backend.service

import com.sorsix.backend.domain.discussions.Comment
import com.sorsix.backend.domain.discussions.Discussion
import com.sorsix.backend.dto.CommentDto
import com.sorsix.backend.dto.DiscussionDto
import com.sorsix.backend.dto.toDiscussionDto
import com.sorsix.backend.exceptions.DiscussionNotFoundException
import com.sorsix.backend.exceptions.DoctorNotFoundException
import com.sorsix.backend.repository.CommentRepository
import com.sorsix.backend.repository.DiscussionRepository
import com.sorsix.backend.repository.UserRepository
import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.service.specification.FieldFilterSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiscussionService(
    val discussionRepository: DiscussionRepository,
    val userRepository: UserRepository,
    val publicCaseService: PublicCaseService,
    val commentRepository: CommentRepository,
    val notificationService: NotificationService
) {
    fun listAll(q: String?, pageable: Pageable): Page<DiscussionDto> {
        val specs = listOfNotNull(
            FieldFilterSpecification.filterContainsText<Discussion>(listOf("title", "description"), q)
        )

        val spec: Specification<Discussion>? = specs.reduceOrNull { acc, s -> acc.and(s) }

        return discussionRepository.findAll(spec, pageable).map { it.toDiscussionDto() }
    }

    fun findById(id: Long) = this.discussionRepository.findById(id)

    fun getDiscussionsByCase(caseId: Long): List<Discussion> {
        return discussionRepository.findAllByPublicCaseId(caseId)
    }

    fun createDiscussion(dto: DiscussionDto): Discussion {
        val user = userRepository.findByIdOrNull(dto.userId)
            ?: throw DoctorNotFoundException(dto.userId)

        val case = publicCaseService.findById(dto.caseId)

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

    fun getCommentsByDiscussion(discussionId: Long) =
        commentRepository.findAllByDiscussionIdAndParentIsNull(discussionId)

    @Transactional
    fun addComment(dto: CommentDto, userDetails: CustomUserDetails): Comment {
        val user = userRepository.findByIdOrNull(userDetails.getId())

        val discussion = discussionRepository.findById(dto.discussionId)
            .orElseThrow { DiscussionNotFoundException(dto.discussionId) }

        val parent = dto.parentId?.let { commentRepository.findByIdOrNull(it) }


        val newComment = commentRepository.save(
            Comment(
                id = null,
                content = dto.content,
                discussion = discussion,
                user = user!!,
                parent = parent
            )
        )

        if (newComment.parent != null) { // REPLY to another comment
            val parentCommentAuthor = newComment.parent!!.user
            if (parentCommentAuthor.id != user.id) {
                val notificationPayload = mapOf(
                    "message" to "${user.email} replied to your comment.",
                    "discussionId" to discussion.id,
                    "commentId" to newComment.id
                )
                notificationService.sendNotificationToUser(parentCommentAuthor.email, notificationPayload)
            }
        } else { // TOP-LEVEL comment on the discussion
            val discussionAuthor = discussion.user
            if (discussionAuthor.id != user.id) {
                val notificationPayload = mapOf(
                    "message" to "${user.email} commented on your discussion: '${discussion.title}'",
                    "discussionId" to discussion.id,
                    "commentId" to newComment.id
                )
                notificationService.sendNotificationToUser(discussionAuthor.email, notificationPayload)
            }
        }
        return newComment
    }
}