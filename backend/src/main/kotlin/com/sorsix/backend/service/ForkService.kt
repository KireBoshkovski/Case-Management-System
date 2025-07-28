package com.sorsix.backend.service

import com.sorsix.backend.repository.ForkRepository
import org.springframework.stereotype.Service

@Service
class ForkService(
    val forkRepoistory: ForkRepository
)