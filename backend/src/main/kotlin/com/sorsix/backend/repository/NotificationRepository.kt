package com.sorsix.backend.repository

import com.sorsix.backend.domain.notifications.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findByUserIdOrderByTimestampDesc(userId: Long): List<Notification>

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.userId = :userId AND n.read = false")
    fun markAllAsRead(@Param("userId") userId: Long)

    fun deleteNotificationByIdAndUserId(notificationId: Long, userId: Long)

    fun deleteNotificationsByReadTrueAndUserId(userId: Long)
}