package com.sorsix.backend.dto

data class CreateThreadRequest(
    val caseId: Long,
    val creatingDoctorId: Long,
    val title: String
)