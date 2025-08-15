package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.PublicExamination
import org.springframework.data.jpa.repository.JpaRepository

interface PublicExaminationRepository : JpaRepository<PublicExamination, Long>