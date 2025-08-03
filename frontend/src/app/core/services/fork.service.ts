import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Fork } from '../../models/fork';

@Injectable({
    providedIn: 'root',
})
export class ForkService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getFork(id: number): Observable<Fork> {
        return this.http.get<Fork>(`${this.apiUrl}/forks/${id}`);
    }

    getForksByCaseId(id: number): Observable<Fork[]> {
        return this.http.get<Fork[]>(`${this.apiUrl}/forks/origin/${id}`);
    }
}
