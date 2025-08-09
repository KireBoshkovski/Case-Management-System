package com.sorsix.backend.dto

import com.sorsix.backend.domain.Case
import com.sorsix.backend.domain.Examination
import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.domain.users.Patient
import java.time.LocalDateTime

data class CaseDto(
    val id: Long,
    val bloodType: String?,
    val allergies: String?,
    val description: String?,
    val treatmentPlan: String?,
    val status: CaseStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val patient: Patient,
    val doctorId: Long,
    val examinations: List<Examination>
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
    patient = this.patient,
    doctorId = this.doctor.id,
    examinations = this.examinations
)

