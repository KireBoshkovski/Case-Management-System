import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { CommentDto } from '../../../models/discussions/comment-dto';
import { CommentInput } from '../comment-input/comment-input';

@Component({
    selector: 'comment',
    imports: [CommentInput],
    templateUrl: './comment.html',
    styleUrl: './comment.css',
})
export class Comment {
    @Input() comment!: CommentDto;
    @Output() replyAdded = new EventEmitter<{
        parentId: number;
        content: string;
    }>();

    showReplyInput = false;

    toggleReplyInput(): void {
        this.showReplyInput = !this.showReplyInput;
    }

    onReplySubmitted(replyContent: string) {
        console.log('Comment component', replyContent);

        this.replyAdded.emit({
            parentId: this.comment.id!,
            content: replyContent,
        });

        this.showReplyInput = false;
    }
}
