package com.sorsix.backend.service

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.domain.discussions.Comment
import com.sorsix.backend.domain.discussions.Discussion
import com.sorsix.backend.domain.notifications.Notification
import com.sorsix.backend.domain.notifications.NotificationType
import com.sorsix.backend.domain.users.User
import com.sorsix.backend.dto.CommentDto
import com.sorsix.backend.dto.DiscussionCreateRequest
import com.sorsix.backend.dto.DiscussionDto
import com.sorsix.backend.dto.toDiscussionDto
import com.sorsix.backend.exceptions.DiscussionNotFoundException
import com.sorsix.backend.exceptions.DoctorNotFoundException
import com.sorsix.backend.repository.CommentRepository
import com.sorsix.backend.repository.DiscussionRepository
import com.sorsix.backend.repository.UserRepository
import com.sorsix.backend.service.specification.FieldFilterSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime

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

    @Transactional
    fun createDiscussion(request: DiscussionCreateRequest, userId: Long): DiscussionDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw DoctorNotFoundException(userId)

        val publicCase = publicCaseService.findById(request.caseId)

        val entity = Discussion(
            id = null,
            title = request.title,
            description = request.description,
            user = user,
            publicCase = publicCase
        )

        val saved = discussionRepository.save(entity)

        return DiscussionDto(
            id = saved.id!!,
            title = saved.title,
            description = saved.description,
            createdAt = Instant.now(),
            userId = saved.user.id,
            caseId = saved.publicCase.id!!,
            commentsCount = saved.comments.size
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

        val recipient: User
        val notificationType: NotificationType
        val message: String

        if (newComment.parent != null) { // REPLY to another comment
            val parentCommentAuthor = newComment.parent!!.user
            if (parentCommentAuthor.id == user.id) {
                return newComment
            }
            recipient = parentCommentAuthor
            notificationType = NotificationType.COMMENT_REPLY
            message = "${user.email} replied to your comment."
        } else {
            val discussionAuthor = discussion.user
            if (discussionAuthor.id == user.id) {
                return newComment
            }
            recipient = discussionAuthor
            notificationType = NotificationType.DISCUSSION_COMMENT
            message = "${user.email} commented on your discussion ${discussion.title}."
        }

        val notification = notificationService.save(
            Notification(
                null,
                recipient.id,
                notificationType,
                message,
                discussion.id!!,
                newComment.id,
                user.id,
                LocalDateTime.now(),
                false
            )
        )
        notificationService.sendNotificationToUser(recipient.email, notification)
        return newComment
    }
}