import { Component, Input } from '@angular/core';
import { CommentDto } from '../../../models/discussions/comment-dto';

@Component({
    selector: 'comment',
    imports: [],
    templateUrl: './comment.html',
    styleUrl: './comment.css',
})
export class Comment {
    @Input() comment!: CommentDto;
}
