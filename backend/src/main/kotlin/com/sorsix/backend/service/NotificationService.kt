package com.sorsix.backend.service

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.domain.notifications.Notification
import com.sorsix.backend.repository.NotificationRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    val messagingTemplate: SimpMessagingTemplate,
    val notificationRepository: NotificationRepository
) {
    /**
     * Sends a notification to a specific user.
     * @param userId The ID of the user to notify.
     * @param notificationPayload The content of the notification.
     * The destination is /user/{userId}/queue/notifications
     * Spring automatically maps this to the correct user session.
     */
    fun sendNotificationToUser(userId: String, notification: Notification) =
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification)

    fun findByUser(id: Long): List<Notification> = notificationRepository.findByUserIdOrderByTimestampDesc(id)

    fun markAsRead(id: Long, userDetails: CustomUserDetails) {
        val notification = notificationRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Notification not found") }

        // Only allow marking notifications of the current user
        if (notification.userId != userDetails.getId()) {
            throw IllegalAccessException("Cannot mark another user's notification as read")
        }

        notification.read = true
        notificationRepository.save(notification)
    }

    fun save(notification: Notification) = notificationRepository.save(notification)

    @Transactional
    fun markAllAsRead(id: Long) = notificationRepository.markAllAsRead(id)

    @Transactional
    fun removeNotification(notificationId: Long, userId: Long) =
        notificationRepository.deleteNotificationByIdAndUserId(notificationId, userId)

    @Transactional
    fun removeNotifications(id: Long) = notificationRepository.deleteNotificationsByReadTrueAndUserId(id)
}