package com.sorsix.backend.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sorsix.backend.domain.Case
import com.sorsix.backend.dto.CensoredCaseDto
import com.sorsix.backend.dto.CensoredExaminationDto
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.stereotype.Service


@Service
class OpenAIService(
    private val chatModel: OpenAiChatModel,
    val caseService: CaseService
) {
    private val objectMapper = jacksonObjectMapper()

    fun censor(id: Long): Case {
        val originalCase = caseService.findById(id)

        val caseDto = CensoredCaseDto(
            allergies = originalCase.allergies,
            description = originalCase.description,
            treatmentPlan = originalCase.treatmentPlan,
            examinations = originalCase.examinations.map { examination ->
                CensoredExaminationDto(
                    findings = examination.findings,
                    results = examination.results,
                    notes = examination.notes
                )
            }
        )

        val jsonInput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(caseDto)

        val promptMessage = """
            You are a helpful assistant designed to redact personal information from medical records to prepare them for public case studies.
            Review the following JSON data. Remove all Personally Identifiable Information (PII), such as patient names, doctor names (if mentioned in the text), specific locations, phone numbers, detailed dates, or any other identifying data. Replace them with a generic placeholder like "[CENSORED]".

            IMPORTANT: Return ONLY the modified JSON object with the exact same structure as the input. Do not include any extra text, explanations, or markdown formatting.

            JSON data to be censored:
            $jsonInput
            """.trimIndent()

        val censoredJsonResponse = generate(promptMessage)
            ?: throw RuntimeException("Failed to get response from OpenAI")

        val censoredResponse = try {
            objectMapper.readValue<CensoredCaseDto>(censoredJsonResponse.trim())
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse censored JSON response: ${e.message}", e)
        }

        val censoredCase = originalCase.copy(
            allergies = censoredResponse.allergies,
            description = censoredResponse.description,
            treatmentPlan = censoredResponse.treatmentPlan,
            examinations = originalCase.examinations.mapIndexed { index, examination ->
                val censoredExam = censoredResponse.examinations.getOrNull(index)
                examination.copy(
                    findings = censoredExam?.findings ?: examination.findings,
                    results = censoredExam?.results ?: examination.results,
                    notes = censoredExam?.notes ?: examination.notes
                )
            }.toMutableList()
        )
        return censoredCase
    }

    private fun generate(message: String): String? {
        val response = chatModel.call(
            Prompt(listOf(UserMessage(message)))
        )
        return response.result.output.text
    }
}