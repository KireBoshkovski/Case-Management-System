package com.sorsix.backend.web

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.domain.notifications.Notification
import com.sorsix.backend.service.NotificationService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    val notificationService: NotificationService
) {
    // Fetch all notifications for the logged-in user
    @GetMapping
    fun getNotifications(@AuthenticationPrincipal userDetails: CustomUserDetails): List<Notification> =
        notificationService.findByUser(userDetails.getId())

    @PutMapping("/{id}/read")
    fun readNotification(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) = notificationService.markAsRead(id, userDetails)

    @PutMapping
    fun markAllRead(@AuthenticationPrincipal userDetails: CustomUserDetails) =
        notificationService.markAllAsRead(userDetails.getId())

    @DeleteMapping("/{id}")
    fun removeNotification(@PathVariable id: Long, @AuthenticationPrincipal userDetails: CustomUserDetails) =
        notificationService.removeNotification(id, userDetails.getId())

    @DeleteMapping("/read")
    fun removeAllRead(@AuthenticationPrincipal userDetails: CustomUserDetails) =
        notificationService.removeNotifications(userDetails.getId())
}