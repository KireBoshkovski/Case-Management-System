import { Component, inject } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'logout',
    imports: [],
    templateUrl: './logout.html',
    styleUrl: './logout.css',
})
export class Logout {
    authService = inject(AuthService);
    router = inject(Router);

    constructor() {
        this.authService.logout();
        setTimeout(() => {
            this.router.navigate(['/auth/login']);
        }, 2000);
    }
}
