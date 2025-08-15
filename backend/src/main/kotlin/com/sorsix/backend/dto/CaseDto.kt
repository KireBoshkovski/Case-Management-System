package com.sorsix.backend.dto

import com.sorsix.backend.domain.cases.Case
import com.sorsix.backend.domain.enums.CaseStatus
import java.time.LocalDateTime

data class CaseDto(
    val id: Long?,
    val bloodType: String?,
    val allergies: String?,
    val description: String?,
    val treatmentPlan: String?,
    val status: CaseStatus,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val patientId: Long,
    val doctorId: Long,
    val examinationsIds: List<Long?>
)

fun Case.toCaseDto() = CaseDto(
    id = this.id,
    bloodType = this.bloodType,
    allergies = this.allergies,
    description = this.description,
    treatmentPlan = this.treatmentPlan,
    status = this.status,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    patientId = this.patient.id,
    doctorId = this.doctor.id,
    examinationsIds = this.examinations.map { it.id }
)

