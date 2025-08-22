package com.sorsix.backend.web

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.domain.notifications.Notification
import com.sorsix.backend.repository.NotificationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(
    val notificationService: NotificationRepository,
) {
    // Fetch all notifications for the logged-in user
    @GetMapping("/notifications")
    fun getNotifications(@AuthenticationPrincipal userDetails: CustomUserDetails): List<Notification> {
        return notificationService.findByUserIdOrderByTimestampDesc(userDetails.getId())
    }

    // Fetch only unread notifications
    @GetMapping("/notifications/unread")
    fun getUnreadNotifications(@AuthenticationPrincipal userDetails: CustomUserDetails): List<Notification> {
        return notificationService.findByUserIdAndReadFalse(userDetails.getId())
    }
}