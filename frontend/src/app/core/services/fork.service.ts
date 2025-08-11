import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Fork } from '../../models/forks/fork';

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

    createFork(newFork: Partial<Fork>): Observable<Fork> {
        return this.http.post<Fork>(`${this.apiUrl}/forks`, newFork);
    }

    updateFork(id: number, updatedFork: Partial<Fork>): Observable<Fork> {
        return this.http.put<Fork>(`${this.apiUrl}/forks/${id}`, updatedFork);
    }

    deleteFork(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/forks/${id}`);
    }
}
