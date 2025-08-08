import {inject, Injectable} from '@angular/core';
import {environment} from '../../../environments/environments';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PageResponse} from '../../models/page-response.model';
import {CaseDto} from '../../models/case.dto';
import {Visibility} from '../../models/visibility.type';
import {Case} from '../../models/case.model';

export interface GetCasesOptions {
    visibility?: Visibility;
    patientId?: number;
    page?: number;
    size?: number;
    sort?: string[];
}

@Injectable({
    providedIn: 'root',
})
export class CaseService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getCases(options: GetCasesOptions = {}): Observable<PageResponse<CaseDto>> {
        const {
            visibility = 'ALL',
            patientId,
            page = 0,
            size = 20,
            sort = ['createdAt,desc'],
        } = options;

        let params = new HttpParams()
            .set('visibility', visibility)
            .set('page', page)
            .set('size', size);

        sort.forEach(s => (params = params.append('sort', s)));
        if (patientId != null) params = params.set('patientId', patientId);

        return this.http.get<PageResponse<CaseDto>>(`${this.apiUrl}/cases`, {params});
    }

    getPublicCasesPaged(page = 0, size = 20): Observable<PageResponse<CaseDto>> {
        return this.getCases({ visibility: 'PUBLIC', page, size });
    }

    getCasesByPatientIdPaged(patientId: number, page = 0, size = 20): Observable<PageResponse<CaseDto>> {
        return this.getCases({ patientId, page, size });
    }

    getCaseById(id: number): Observable<Case> {
        return this.http.get<Case>(`${this.apiUrl}/cases/${id}`);
    }

}
