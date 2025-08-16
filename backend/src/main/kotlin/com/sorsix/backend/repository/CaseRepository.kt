package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.Case
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CaseRepository : JpaRepository<Case, Long>, JpaSpecificationExecutor<Case>