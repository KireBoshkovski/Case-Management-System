package com.sorsix.backend.web

import com.sorsix.backend.service.ThreadDiscussionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/discussions")
@CrossOrigin(origins = ["http://localhost:4200"])
class ThreadDiscussionController(
    private val discussionService: ThreadDiscussionService
) {

    //addDiscussion

    //getDiscussionsByThread
}