import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DiscussionService } from '../../../core/services/discussion.service';
import { CommentDto } from '../../../models/discussions/comment-dto';
import { Comment } from '../comment/comment';
import { CommentInput } from '../comment-input/comment-input';
import { NgClass } from '@angular/common';

@Component({
    selector: 'discussion-comments',
    imports: [Comment, CommentInput, NgClass],
    templateUrl: './discussion-comments.html',
    styleUrl: './discussion-comments.css',
})
export class DiscussionComments implements OnInit {
    route = inject(ActivatedRoute);
    discussionService = inject(DiscussionService);

    discussionId!: number;
    comments: CommentDto[] = [];
    showCommentInput = false;

    data: DiscussionDetails | undefined;

    ngOnInit() {
        this.discussionId = +this.route.snapshot.paramMap.get('id')!;

        this.discussionService.getDiscussionById(this.discussionId).subscribe({
            next: (data) => {
                console.log(data);

                this.data = data;
            },
            error: (err) => console.error(err),
        });

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

    onCommentSubmitted(content: string) {
        const newComment: CommentDto = {
            content: content,
            discussionId: this.discussionId,
        };

        this.discussionService.addComment(newComment).subscribe({
            next: (createdComment: CommentDto) => {
                console.log('Reply added successfully:', createdComment);

                this.showCommentInput = false;
                this.comments.unshift(createdComment);
            },
            error: (error) => {
                console.error('Error adding reply:', error);
            },
        });
    }

    handleReplyAdded($event: { parentId: number; content: string }) {
        console.log($event);

        const newComment: CommentDto = {
            discussionId: this.discussionId,
            content: $event.content,
            parentId: $event.parentId,
        };

        console.log(newComment);

        this.discussionService.addComment(newComment).subscribe({
            next: (createdComment: CommentDto) => {
                this.showCommentInput = false;

                console.log('Reply added successfully:', createdComment);

                this.addReplyToLocalComments(createdComment);
            },
            error: (error) => {
                console.error('Error adding reply:', error);
            },
        });
    }

    private addReplyToLocalComments(newReply: CommentDto): void {
        // Find the parent comment and add the reply locally
        this.addReplyToComment(this.comments, newReply.parentId!, newReply);
    }

    private addReplyToComment(
        comments: CommentDto[],
        parentId: number,
        reply: CommentDto,
    ): boolean {
        for (const comment of comments) {
            if (comment.id === parentId) {
                if (!comment.replies) {
                    comment.replies = [];
                }
                comment.replies.push(reply);
                return true;
            }

            // Recursively search in nested replies
            if (
                comment.replies &&
                this.addReplyToComment(comment.replies, parentId, reply)
            ) {
                return true;
            }
        }
        return false;
    }

    toggleCommentInput() {
        this.showCommentInput = !this.showCommentInput;
    }
}
