import { Component, inject } from '@angular/core';
import { NotificationService } from '../../../core/services/notification.service';
import { AsyncPipe } from '@angular/common';
import { Notification } from '../../../models/discussions/notification';
import { Router } from '@angular/router';
import { filter, finalize, map } from 'rxjs';

@Component({
    selector: 'notification-bell',
    imports: [AsyncPipe],
    templateUrl: './notification-bell.html',
    styleUrl: './notification-bell.css',
})
export class NotificationBell {
    notificationService = inject(NotificationService);
    router = inject(Router);

    notifications$ = this.notificationService.getNotifications();
    unreadCount$ = this.notificationService.getUnreadCount();

    markRead(id: number) {
        this.notificationService.markAsRead(id);
    }

    markAllAsRead() {
        this.notificationService.markAllAsRead();
    }

    refreshNotifications() {
        this.notificationService.getNotificationsFromApi();
        this.notifications$ = this.notificationService.getNotifications();
    }

    onNotificationClick(notification: Notification) {
        if (!notification.read) {
            this.markRead(notification.id);
        }

        if (notification.discussionId) {
            this.router.navigate(['/discussions', notification.discussionId]);
        }
    }

    removeNotification(id: number) {
        this.notificationService.deleteNotification(id);
    }

    removeAllReadNotifications() {
        this.notificationService.deleteReadNotifications();
    }

    openedMessages() {
        return this.notifications$.pipe(
            map((notifications) => notifications.filter((n) => n.read).length),
        );
    }
}
