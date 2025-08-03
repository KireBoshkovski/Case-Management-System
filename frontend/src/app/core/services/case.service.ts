import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Case } from '../../models/case.model';

@Injectable({
    providedIn: 'root',
})
export class CaseService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getCases(): Observable<Case[]> {
        return this.http.get<Case[]>(`${this.apiUrl}/cases`);
    }

    getPublicCases() {
        return this.http.get<Case[]>(`${this.apiUrl}/cases/public`);
    }

    getCaseById(id: number): Observable<Case> {
        return this.http.get<Case>(`${this.apiUrl}/cases/${id}`);
    }

    getCasesByPatientId(id: number): Observable<Case[]> {
        return this.http.get<Case[]>(`${this.apiUrl}/cases/patient/${id}`);
    }
}
