package com.sorsix.backend.web

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.domain.discussions.Discussion
import com.sorsix.backend.dto.*
import com.sorsix.backend.service.DiscussionService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/discussions")
class DiscussionController(
    val discussionService: DiscussionService
) {
    @GetMapping
    fun listAll(
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable

    ) = discussionService.listAll(q, pageable)

    @GetMapping("/{discussionId}")
    fun getDiscussionById(@PathVariable discussionId: Long) =
        ResponseEntity.ok(discussionService.findById(discussionId).map { it.details() })

    @GetMapping("/case/{caseId}")
    fun getDiscussionsByCase(@PathVariable caseId: Long) =
        discussionService.getDiscussionsByCase(caseId).map { it.toDiscussionDto() }


    @PostMapping
    fun createDiscussion(@RequestBody dto: DiscussionDto): ResponseEntity<Discussion> {
        return ResponseEntity.ok(discussionService.createDiscussion(dto))
    }

    @GetMapping("/{discussionId}/comments")
    fun getCommentsByDiscussion(@PathVariable discussionId: Long) =
        discussionService.getCommentsByDiscussion(discussionId).map { it.toDto() }

    @PostMapping("/{discussionId}/comments")
    fun addComment(
        @PathVariable discussionId: Long,
        @RequestBody dto: CommentDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<CommentDto> =
        ResponseEntity.ok(discussionService.addComment(dto.copy(discussionId = discussionId), userDetails).toDto())
}
