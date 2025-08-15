import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentDto } from '../../models/discussions/comment-dto';
import { DiscussionDto } from '../../models/discussions/discussion-dto';

@Injectable({
    providedIn: 'root',
})
export class DiscussionService {
    private apiUrl = environment.apiUrl;
    http = inject(HttpClient);

    getDiscussionsByCase(caseId: number): Observable<DiscussionDto[]> {
        return this.http.get<DiscussionDto[]>(`${this.apiUrl}/discussions/case/${caseId}`);
    }

    getCommentsByDiscussion(discussionId: number): Observable<CommentDto[]> {
        return this.http.get<CommentDto[]>(
            `${this.apiUrl}/discussions/${discussionId}/comments`,
        );
    }

    addComment(
        discussionId: number,
        comment: CommentDto,
    ): Observable<CommentDto> {
        return this.http.post<CommentDto>(
            `${this.apiUrl}/discussions/${discussionId}/comments`,
            comment,
        );
    }
}
