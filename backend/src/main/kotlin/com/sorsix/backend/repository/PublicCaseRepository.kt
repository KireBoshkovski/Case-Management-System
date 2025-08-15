package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.PublicCase
import org.springframework.data.jpa.repository.JpaRepository

interface PublicCaseRepository : JpaRepository<PublicCase, Long>