import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    return authService.isLoggedIn$.pipe(
        take(1), // Take the latest value and complete
        map((isLoggedIn) => {
            if (isLoggedIn) {
                return true; // Allow access
            }
            // Redirect to the login page
            return router.createUrlTree(['/auth/login']);
        })
    );
};
