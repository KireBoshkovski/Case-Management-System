import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentDto } from '../../models/discussions/comment-dto';
import { DiscussionDto } from '../../models/discussions/discussion-dto';
import { PageResponse } from '../../models/page-response';
import { CaseDto } from '../../models/cases/case.dto';
import { GetCasesOptions } from '../../models/cases-options';

@Injectable({
    providedIn: 'root',
})
export class DiscussionService {
    private apiUrl = environment.apiUrl;
    http = inject(HttpClient);

    getDiscussions(
        options: GetCasesOptions,
    ): Observable<PageResponse<DiscussionDto>> {
        const {
            patientId,
            page = 1,
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

        return this.http.get<PageResponse<DiscussionDto>>(
            `${this.apiUrl}/discussions`,
            {
                params,
            },
        );
    }

    getDiscussionsByCase(caseId: number): Observable<DiscussionDto[]> {
        return this.http.get<DiscussionDto[]>(
            `${this.apiUrl}/discussions/case/${caseId}`,
        );
    }

    getDiscussionById(discussionId: number) {
        return this.http.get<DiscussionDetails>(`${this.apiUrl}/discussions/${discussionId}`);
    }

    getCommentsByDiscussion(discussionId: number): Observable<CommentDto[]> {
        return this.http.get<CommentDto[]>(
            `${this.apiUrl}/discussions/${discussionId}/comments`,
        );
    }

    addComment(comment: CommentDto): Observable<CommentDto> {
        return this.http.post<CommentDto>(
            `${this.apiUrl}/discussions/${comment.discussionId}/comments`,
            comment,
        );
    }
}
