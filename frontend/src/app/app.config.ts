import {
    ApplicationConfig,
    provideBrowserGlobalErrorListeners,
    provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { RxStomp } from '@stomp/rx-stomp';

import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth-interceptor';

import { provideToastr } from 'ngx-toastr';

export function rxStompFactory() {
    return new RxStomp();
}

export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes),
        provideHttpClient(withInterceptors([authInterceptor])),
        { provide: RxStomp, useFactory: rxStompFactory },
        provideToastr({
            timeOut: 5000,
            positionClass: 'toast-bottom-right',
            preventDuplicates: true,
            progressBar: true,
            closeButton: true,
        }),
    ],
};
