import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Case } from '../../models/cases/case.model';
import { PublicCase } from '../../models/cases/public-case.model';
import { CaseDto } from '../../models/cases/case-dto.model';
import { PageResponse } from '../../models/page-response';
import { GetCasesOptions } from '../../models/cases-options';

@Injectable({
    providedIn: 'root',
})
export class CaseService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getCases(options: GetCasesOptions = {}): Observable<PageResponse<CaseDto>> {
        const {
            patientId,
            page = 0,
            size = 20,
            sort = ['createdAt,desc'],
            query,
        } = options;

        let params = new HttpParams().set('page', page).set('size', size);

        sort.forEach((s) => (params = params.append('sort', s)));
        if (patientId != null) params = params.set('patientId', patientId);
        if (query && query.trim().length > 0)
            params = params.set('q', query.trim());

        console.log('Request with params:', params);

        return this.http.get<PageResponse<CaseDto>>(`${this.apiUrl}/cases`, {
            params,
        });
    }

    getCaseById(id: number) {
        return this.http.get<Case>(`${this.apiUrl}/cases/${id}`);
    }

    getPublicCases(
        options: GetCasesOptions = {},
    ): Observable<PageResponse<PublicCase>> {
        const {
            page = 0,
            size = 20,
            sort = ['createdAt,desc'],
            query,
        } = options;

        let params = new HttpParams().set('page', page).set('size', size);

        sort.forEach((s) => (params = params.append('sort', s)));
        if (query && query.trim().length > 0)
            params = params.set('q', query.trim());

        console.log('Request with params:', params);

        return this.http.get<PageResponse<PublicCase>>(
            `${this.apiUrl}/public`,
            { params },
        );
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
