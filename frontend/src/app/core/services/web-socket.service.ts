import { inject, Injectable } from '@angular/core';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import { Message } from '@stomp/stompjs';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class WebSocketService {
    private rxStomp = inject(RxStomp);

    connect(jwtToken: string) {
        if (this.rxStomp.active) {
            return;
        }
        this.rxStomp.configure({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: {
                Authorization: `Bearer ${jwtToken}`,
            },
            heartbeatIncoming: 0,
            heartbeatOutgoing: 20000,
            reconnectDelay: 5000,
            debug: (msg: string): void => {
                console.log(new Date(), msg);
            },
        });
        this.rxStomp.activate();
    }

    disconnect(): void {
        this.rxStomp.deactivate();
    }

    watchNotifications(): Observable<Message> {
        return this.rxStomp.watch('/user/queue/notifications');
    }
}
