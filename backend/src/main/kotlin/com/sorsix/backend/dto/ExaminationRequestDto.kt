package com.sorsix.backend.dto

data class ExaminationRequestDto(
    val examinationType: String,
    val findings: String?,
    val results: String?,
    val notes: String?,
    val vitalSigns: String?,
    val examinationDate: String,
)
