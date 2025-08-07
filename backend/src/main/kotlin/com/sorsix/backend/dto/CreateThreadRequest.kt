package com.sorsix.backend.dto

data class CreateThreadRequest(
    val caseId: Long,
    val title: String,
    val creatingDoctorId: Long,
)