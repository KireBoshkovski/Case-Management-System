import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../../models/security/login-request';
import { Observable, tap } from 'rxjs';
import { JwtResponse } from '../../models/security/jwt-response';
import { SignupRequest } from '../../models/security/signup-request';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    signIn(LoginRequest: LoginRequest): Observable<JwtResponse> {
        return this.http
            .post<JwtResponse>(`${this.apiUrl}/auth/signin`, LoginRequest)
            .pipe(
                tap((response) => {
                    localStorage.setItem('token', response.accessToken);
                }),
            );
    }

    signUp(signUpRequest: SignupRequest): Observable<{ message: string }> {
        return this.http.post<{ message: string }>(
            `${this.apiUrl}/auth/signup`,
            signUpRequest,
        );
    }

    logout(): void {
        localStorage.removeItem('token');
    }

    getToken(): string | null {
        const token = localStorage.getItem('token');
        if (!token) return null;
        if (this.isTokenExpired(token)) {
            this.logout();
            return null;
        }

        return token;
    }

    isAuthenticated(): boolean {
        const token = this.getToken();
        return !!token;
    }

    private decodePayload(token: string): any {
        try {
            const payload = token.split('.')[1];
            return JSON.parse(atob(payload));
        } catch (e) {
            return null;
        }
    }

    private isTokenExpired(token: string): boolean {
        const payload = this.decodePayload(token);
        if (!payload || !payload.exp) return true;

        const expiryTime = payload.exp * 1000;
        return Date.now() > expiryTime;
    }
}
