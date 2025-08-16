package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.PublicCase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PublicCaseRepository : JpaRepository<PublicCase, Long>, JpaSpecificationExecutor<PublicCase>