import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { WebSocketService } from './web-socket.service';
import { environment } from '../../../environments/environments';
import { Notification } from '../../models/discussions/notification';

@Injectable({
    providedIn: 'root',
})
export class NotificationService {
    private apiUrl = environment.apiUrl;

    private notificationsSubject = new BehaviorSubject<Notification[]>([]);
    notifications$ = this.notificationsSubject.asObservable(); // Public observable

    private unreadCountSubject = new BehaviorSubject<number>(0);
    unreadCount$ = this.unreadCountSubject.asObservable(); // Public observable

    private ws = inject(WebSocketService);
    private http = inject(HttpClient);

    constructor() {
        this.getNotificationsFromApi();
    }

    getNotifications() {
        return this.notifications$;
    }

    getUnreadCount() {
        return this.unreadCount$;
    }

    getNotificationsFromApi() {
        this.http
            .get<Notification[]>(`${this.apiUrl}/notifications`)
            .subscribe((data) => {
                this.updateState(data);
            });
    }

    private updateState(notifications: Notification[] | null) {
        const safeNotifications = notifications ?? [];

        this.notificationsSubject.next(safeNotifications);
        const unread = safeNotifications.filter((n) => !n.read).length;
        this.unreadCountSubject.next(unread);
    }

    markAsRead(notificationId: number) {
        this.http
            .put(`${this.apiUrl}/notifications/${notificationId}/read`, {})
            .subscribe(() => {
                const updated = this.notificationsSubject.value.map((n) =>
                    n.id === notificationId ? { ...n, read: true } : n,
                );
                this.updateState(updated);
            });
    }

    markAllAsRead() {
        this.http.put(`${this.apiUrl}/notifications`, {}).subscribe({
            next: () => {
                const updated = this.notificationsSubject.value.map((n) => ({
                    ...n,
                    read: true,
                }));
                this.updateState(updated);
            },
            error: (error) => {
                console.error(error);
            },
        });
    }

    deleteNotification(id: number) {
        this.http
            .delete<Notification[]>(`${this.apiUrl}/notifications/${id}`)
            .subscribe((data) => {
                const updated = this.notificationsSubject.value.filter(
                    (n) => n.id !== id,
                );
                this.updateState(updated);
            });
    }

    deleteReadNotifications() {
        this.http
            .delete<Notification[]>(`${this.apiUrl}/notifications/read`)
            .subscribe(() => {
                const updated = this.notificationsSubject.value.filter(
                    (n) => !n.read,
                );
                this.updateState(updated);
            });
    }
}
