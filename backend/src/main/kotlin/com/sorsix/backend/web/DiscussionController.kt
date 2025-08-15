package com.sorsix.backend.web

import com.sorsix.backend.domain.discussions.Comment
import com.sorsix.backend.domain.discussions.Discussion
import com.sorsix.backend.dto.CommentDto
import com.sorsix.backend.dto.DiscussionDto
import com.sorsix.backend.dto.toDiscussionDto
import com.sorsix.backend.dto.toDto
import com.sorsix.backend.service.DiscussionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/discussions")
class DiscussionController(
    private val discussionService: DiscussionService
) {

    @GetMapping("/case/{caseId}")
    fun getDiscussionsByCase(@PathVariable caseId: Long): ResponseEntity<List<DiscussionDto>> {
        return ResponseEntity.ok(discussionService.getDiscussionsByCase(caseId).map { it.toDiscussionDto() }
        )
    }

    @PostMapping
    fun createDiscussion(@RequestBody dto: DiscussionDto): ResponseEntity<Discussion> {
        return ResponseEntity.ok(discussionService.createDiscussion(dto))
    }

    @GetMapping("/{discussionId}/comments")
    fun getCommentsByDiscussion(@PathVariable discussionId: Long): ResponseEntity<List<CommentDto>> {
        return ResponseEntity.ok(discussionService.getCommentsByDiscussion(discussionId).map { it.toDto() })
    }

    @PostMapping("/{discussionId}/comments")
    fun addComment(@PathVariable discussionId: Long, @RequestBody dto: CommentDto): ResponseEntity<CommentDto> {
        val result = discussionService.addComment(dto.copy(discussionId = discussionId))
        return ResponseEntity.ok(result.toDto())
    }
}
