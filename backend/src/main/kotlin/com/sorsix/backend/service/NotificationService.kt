package com.sorsix.backend.service

import com.sorsix.backend.repository.NotificationRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    val notificationRepository: NotificationRepository,
    val messagingTemplate: SimpMessagingTemplate
) {
    /**
     * Sends a notification to a specific user.
     * @param userId The ID of the user to notify.
     * @param notificationPayload The content of the notification.
     */
    fun sendNotificationToUser(userId: String, notificationPayload: Any) {
        // The destination is /user/{userId}/queue/notifications
        // Spring automatically maps this to the correct user session.
        messagingTemplate.convertAndSendToUser(
            userId,
            "/queue/notifications",
            notificationPayload
        )
    }
}