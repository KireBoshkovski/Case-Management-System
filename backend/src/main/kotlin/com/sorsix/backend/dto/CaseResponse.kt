package com.sorsix.backend.dto

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.Patient
import com.sorsix.backend.domain.enums.CaseStatus
import java.time.LocalDateTime

data class CaseResponse(
    val id: Long,
    val public: Boolean,
    val bloodType: String?,
    val allergies: String?,
    val description: String?,
    val treatmentPlan: String?,
    val status: CaseStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val patient: Patient,
    val doctorId: Long,
    val examinationIds: List<Long>
)

fun Case.toResponseDto() = CaseResponse(
    id = this.id,
    public = this.public,
    bloodType = this.bloodType,
    allergies = this.allergies,
    description = this.description,
    treatmentPlan = this.treatmentPlan,
    status = this.status,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    patient = this.patient,
    doctorId = this.doctor.id,
    examinationIds = this.examinations.map { it.id }
)