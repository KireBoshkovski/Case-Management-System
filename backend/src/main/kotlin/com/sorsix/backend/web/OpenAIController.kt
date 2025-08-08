package com.sorsix.backend.web

import com.sorsix.backend.service.OpenAIService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class OpenAIController(
    val openAIService: OpenAIService
) {

}