import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Case } from '../../models/cases/case.model';
import { PublicCase } from '../../models/cases/public-case.model';
import { CaseDto } from '../../models/cases/case-dto.model';

@Injectable({
    providedIn: 'root',
})
export class CaseService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getCases() {
        return this.http.get<CaseDto[]>(`${this.apiUrl}/cases`);
    }

    getCaseById(id: number) {
        return this.http.get<Case>(`${this.apiUrl}/cases/${id}`);
    }

    getPublicCases() {
        return this.http.get<PublicCase[]>(`${this.apiUrl}/public`);
    }

    getPublicCaseById(id: number) {
        return this.http.get<PublicCase>(`${this.apiUrl}/public/${id}`);
    }

    censorCase(id: number) {
        return this.http.get<Case>(`${this.apiUrl}/cases/censor/${id}`);
    }

    publishCase(id: number, publicCase: PublicCase) {
        return this.http.post<void>(
            `${this.apiUrl}/cases/publish/${id}`,
            publicCase,
        );
    }

    createCase(newCase: Partial<CaseDto>): Observable<CaseDto> {
        return this.http.post<CaseDto>(`${this.apiUrl}/cases`, newCase);
    }

    updateCase(id: number, updatedCase: Partial<CaseDto>): Observable<CaseDto> {
        return this.http.put<CaseDto>(
            `${this.apiUrl}/cases/${id}`,
            updatedCase,
        );
    }
}
