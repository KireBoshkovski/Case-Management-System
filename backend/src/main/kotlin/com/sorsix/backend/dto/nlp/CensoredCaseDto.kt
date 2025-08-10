package com.sorsix.backend.dto.nlp

data class CensoredCaseDto(
    val description: String?,
    val allergies: String?,
    val treatmentPlan: String?,
    val examinations: List<CensoredExaminationDto>
)
