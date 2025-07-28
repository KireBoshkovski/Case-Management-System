package com.sorsix.backend.repository

import com.sorsix.backend.domain.Fork
import org.springframework.data.jpa.repository.JpaRepository

interface ForkRepository : JpaRepository<Fork, Long>