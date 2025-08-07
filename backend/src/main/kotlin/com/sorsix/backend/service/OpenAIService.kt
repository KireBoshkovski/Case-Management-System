package com.sorsix.backend.service

import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.stereotype.Service


@Service
class OpenAIService(
    private val chatModel: OpenAiChatModel
) {

    fun generate(message: String = "Tell me a joke about programmers"): String? {
        val response = chatModel.call(
            Prompt(listOf(UserMessage(message)))
        )
        return response.result.output.text
    }
}