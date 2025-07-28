package com.sorsix.backend.repository

import com.sorsix.backend.domain.Examination
import org.springframework.data.jpa.repository.JpaRepository

interface ExaminationRepository : JpaRepository<Examination, Long>