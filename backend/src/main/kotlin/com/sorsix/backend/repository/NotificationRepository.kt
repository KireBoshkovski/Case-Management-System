package com.sorsix.backend.repository

import com.sorsix.backend.domain.notifications.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findByUserIdOrderByTimestampDesc(userId: Long): List<Notification>
    fun findByUserIdAndReadFalse(userId: Long): List<Notification>
    fun countByUserIdAndReadFalse(userId: Long): Long
}