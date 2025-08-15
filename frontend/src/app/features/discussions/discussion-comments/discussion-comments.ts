import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DiscussionService } from '../../../core/services/discussion.service';
import { CommentDto } from '../../../models/discussions/comment-dto';
import { Comment } from '../comment/comment';

@Component({
    selector: 'discussion-comments',
    imports: [Comment],
    templateUrl: './discussion-comments.html',
    styleUrl: './discussion-comments.css',
})
export class DiscussionComments implements OnInit {
    route = inject(ActivatedRoute);
    discussionService = inject(DiscussionService);

    discussionId!: number;
    comments: CommentDto[] = [];

    ngOnInit() {
        this.discussionId = +this.route.snapshot.paramMap.get('id')!;

        this.discussionService
            .getCommentsByDiscussion(this.discussionId)
            .subscribe({
                next: (data) => {
                    console.log(data);
                    this.comments = data;
                },
                error: (err) => console.error(err),
            });
    }
}
