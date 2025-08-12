package com.sorsix.backend.dto

import java.time.LocalDateTime

data class ThreadResponse(
    val id: Long,
    val title: String,
    val anonymizedSymptoms: String,
    val anonymizedPatientInfo: String?,
    val status: String,
    val isEducational: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val originalCaseId: Long,
    val creatingDoctorId: Long,
    val parentThreadId: Long?
)