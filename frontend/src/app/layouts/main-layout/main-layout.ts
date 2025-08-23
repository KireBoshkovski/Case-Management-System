import { Component, inject, OnInit } from '@angular/core';
import { Navbar } from '../../shared/components/navbar/navbar';
import { RouterOutlet } from '@angular/router';
import { WebSocketService } from '../../core/services/web-socket.service';
import { AuthService } from '../../core/services/auth.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { ToastrService } from 'ngx-toastr';
import { NotificationService } from '../../core/services/notification.service';

@Component({
    selector: 'main-layout',
    imports: [Navbar, RouterOutlet],
    templateUrl: './main-layout.html',
    styleUrl: './main-layout.css',
})
export class MainLayout implements OnInit {
    webSocketService = inject(WebSocketService);
    authService = inject(AuthService);
    toastr = inject(ToastrService);
    notificationService = inject(NotificationService);

    private notificationSub: Subscription | undefined;

    ngOnInit(): void {
        const token = this.authService.getToken();

        if (token) {
            this.webSocketService.connect(token);

            // Subscribe to the notification channel
            this.notificationSub = this.webSocketService
                .watchNotifications()
                .subscribe((message) => {
                    const notification = JSON.parse(message.body);

                    this.toastr.info(notification.message, 'New Notification');

                    // reload notification bell
                    this.notificationService.getNotificationsFromApi();
                });
        }
    }

    ngOnDestroy(): void {
        this.notificationSub?.unsubscribe();
        this.webSocketService.disconnect();
    }
}
