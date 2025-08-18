package com.sorsix.backend.web

import com.sorsix.backend.domain.discussions.Discussion
import com.sorsix.backend.dto.CommentDto
import com.sorsix.backend.dto.DiscussionDto
import com.sorsix.backend.dto.toDiscussionDto
import com.sorsix.backend.dto.toDto
import com.sorsix.backend.service.DiscussionService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/discussions")
class DiscussionController(
    private val discussionService: DiscussionService
) {
    @GetMapping
    fun listAll(
        @RequestParam(required = false) q: String?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable

    ) = discussionService.listAll(q, pageable)

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
    fun addComment(@PathVariable discussionId: Long, @RequestBody dto: CommentDto): ResponseEntity<CommentDto> {
        val result = discussionService.addComment(dto.copy(discussionId = discussionId))
        return ResponseEntity.ok(result.toDto())
    }
}
