import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../../models/login-request';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { JwtResponse } from '../../models/jwt-response';
import { SignupRequest } from '../../models/signup-request';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    private _isLoggedIn$ = new BehaviorSubject<boolean>(false);
    isLoggedIn$ = this._isLoggedIn$.asObservable();

    signIn(LoginRequest: LoginRequest): Observable<JwtResponse> {
        return this.http
            .post<JwtResponse>(`${this.apiUrl}/auth/signin`, LoginRequest)
            .pipe(
                tap((response) => {
                    this._isLoggedIn$.next(true);

                    localStorage.setItem('access_token', response.accessToken);
                    localStorage.setItem(
                        'refresh_token',
                        response.refreshToken
                    );
                })
            );
    }

    signUp(signUpRequest: SignupRequest): Observable<String> {
        return this.http.post<String>(
            `${this.apiUrl}/auth/signup`,
            signUpRequest
        );
    }

    logout(): void {
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
    }

    getToken(): string | null {
        return localStorage.getItem('access_token');
    }
}
