import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../../environments/environments';
import {PageResponse} from '../../models/page-response.model';


export interface ThreadResponse {
    id: number;
    title: string;
    anonymizedSymptoms: string;
    anonymizedPatientInfo?: string | null;
    status: string;
    isEducational: boolean;
    createdAt: string;
    updatedAt: string;
    originalCaseId: number;
    creatingDoctorId: number;
    parentThreadId?: number | null;
}

export interface CreateThreadRequest {
    caseId: number;
    creatingDoctorId: number;
    title: string;
}

@Injectable({ providedIn: 'root' })
export class ThreadService {
    private http = inject(HttpClient);
    private apiUrl = environment.apiUrl;

    getActive(page = 0, size = 20): Observable<PageResponse<ThreadResponse>> {
        const params = new HttpParams().set('page', page).set('size', size);
        return this.http.get<PageResponse<ThreadResponse>>(`${this.apiUrl}/threads/active`, { params });
    }

    getById(id: number): Observable<ThreadResponse> {
        return this.http.get<ThreadResponse>(`${this.apiUrl}/threads/${id}`);
    }

    getForks(id: number, page = 0, size = 20): Observable<PageResponse<ThreadResponse>> {
        const params = new HttpParams().set('page', page).set('size', size);
        return this.http.get<PageResponse<ThreadResponse>>(`${this.apiUrl}/threads/${id}/forks`, { params });
    }

    create(req: CreateThreadRequest): Observable<ThreadResponse> {
        return this.http.post<ThreadResponse>(this.apiUrl, req);
    }

    fork(id: number, forkingDoctorId: number): Observable<ThreadResponse> {
        const params = new HttpParams().set('forkingDoctorId', forkingDoctorId);
        return this.http.post<ThreadResponse>(`${this.apiUrl}/threads/${id}/fork`, null, { params });
    }

    close(id: number): Observable<ThreadResponse> {
        return this.http.post<ThreadResponse>(`${this.apiUrl}/threads/${id}/close`, null);
    }
}
