import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environments';
import {PageResponse} from '../../models/page-response.model';

export interface DiscussionResponse {
    id: number;
    content: string;
    diagnosisSuggestion?: string | null;
    confidenceLevel?: number | null;
    createdAt: string;
    threadId: number;
    doctorId: number;
    parentDiscussionId?: number | null;
}

export interface CreateDiscussionRequest {
    threadId: number;
    doctorId: number;
    content: string;
    diagnosisSuggestion?: string | null;
    confidenceLevel?: number | null; // 1–10
    parentDiscussionId?: number | null;
}

@Injectable({providedIn: 'root'})
export class DiscussionService {
    private http = inject(HttpClient);
    private apiUrl = environment.apiUrl;

    listByThread(threadId: number, page = 0, size = 50): Observable<PageResponse<DiscussionResponse>> {
        const params = new HttpParams().set('page', page).set('size', size);
        return this.http.get<PageResponse<DiscussionResponse>>(`${this.apiUrl}/discussions/thread/${threadId}`, {params});
    }

    add(req: CreateDiscussionRequest): Observable<DiscussionResponse> {
        return this.http.post<DiscussionResponse>(`${this.apiUrl}/discussions`, req);
    }
}
