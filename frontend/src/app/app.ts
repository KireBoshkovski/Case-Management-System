import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from './core/services/auth.service';
import { WebSocketService } from './core/services/web-socket.service';

@Component({
    selector: 'app-root',
    imports: [RouterOutlet],
    templateUrl: './app.html',
    styleUrl: './app.css',
})
export class App {
    private notificationSub: Subscription | undefined;

    constructor(
        private webSocketService: WebSocketService,
        private authService: AuthService,
    ) {}

    ngOnInit(): void {
        // Assuming your auth service provides the current user's token
        const token = this.authService.getToken();

        if (token) {
            // Connect to WebSocket on app initialization if user is logged in
            this.webSocketService.connect(token);

            // Subscribe to the notification channel
            this.notificationSub = this.webSocketService
                .watchNotifications()
                .subscribe((message) => {
                    // Parse the message body and display the notification
                    const notification = JSON.parse(message.body);
                    console.log('Received notification:', notification);

                    // Here you would trigger a toast, snackbar, or update a notification bell
                    alert(`New Notification: ${notification.message}`);
                });
        }
    }

    ngOnDestroy(): void {
        // Clean up the subscription and disconnect when the app is destroyed
        this.notificationSub?.unsubscribe();
        this.webSocketService.disconnect();
    }
}
