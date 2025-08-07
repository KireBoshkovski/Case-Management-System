package com.sorsix.backend.web

import com.sorsix.backend.service.ThreadDiscussionService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/discussions")
class ThreadDiscussionController(
    private val discussionService: ThreadDiscussionService
) {
    //TODO
    //addDiscussion

    //getDiscussionsByThread
}