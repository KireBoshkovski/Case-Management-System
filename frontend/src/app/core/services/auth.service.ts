import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../../models/security/login-request';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { JwtResponse } from '../../models/security/jwt-response';
import { SignupRequest } from '../../models/security/signup-request';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    private _isLoggedIn$ = new BehaviorSubject<boolean>(this.hasToken());
    isLoggedIn$ = this._isLoggedIn$.asObservable();

    private hasToken(): boolean {
        const token = localStorage.getItem('token');
        return !!token;
    }

    signIn(LoginRequest: LoginRequest): Observable<JwtResponse> {
        return this.http
            .post<JwtResponse>(`${this.apiUrl}/auth/signin`, LoginRequest)
            .pipe(
                tap((response) => {
                    localStorage.setItem('token', response.accessToken);
                    this._isLoggedIn$.next(true);
                }),
            );
    }

    signUp(signUpRequest: SignupRequest): Observable<String> {
        return this.http.post<String>(
            `${this.apiUrl}/auth/signup`,
            signUpRequest,
        );
    }

    logout(): void {
        localStorage.removeItem('token');
        this._isLoggedIn$.next(false);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }
}
