import {inject, Injectable} from '@angular/core';
import {environment} from '../../../environments/environments';
import {HttpClient} from '@angular/common/http';
import {LoginRequest} from '../../models/security/login-request';
import {Observable, tap} from 'rxjs';
import {JwtResponse} from '../../models/security/jwt-response';
import {SignupRequest} from '../../models/security/signup-request';

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
        return localStorage.getItem('token');
    }

    private hasToken(): boolean {
        const token = localStorage.getItem('token');
        return !!token;
    }
}
