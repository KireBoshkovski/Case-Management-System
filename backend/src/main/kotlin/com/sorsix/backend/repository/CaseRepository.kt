package com.sorsix.backend.repository

import com.sorsix.backend.domain.Case
import org.springframework.data.jpa.repository.JpaRepository

interface CaseRepository : JpaRepository<Case, Long>{
    fun findAllByPublicTrue(): List<Case>
    fun findAllByPublicFalse(): List<Case>
}