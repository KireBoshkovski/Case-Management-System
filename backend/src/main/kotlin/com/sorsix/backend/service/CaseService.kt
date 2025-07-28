package com.sorsix.backend.service

import com.sorsix.backend.repository.CaseRepository
import org.springframework.stereotype.Service

@Service
class CaseService(
    val caseRepository: CaseRepository
)