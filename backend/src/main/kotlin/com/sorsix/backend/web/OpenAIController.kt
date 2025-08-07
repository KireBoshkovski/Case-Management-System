package com.sorsix.backend.web

import com.sorsix.backend.service.OpenAIService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class OpenAIController(
    val openAIService: OpenAIService
) {

    @GetMapping
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok().body(openAIService.generate())
    }
}