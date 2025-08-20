import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
    selector: 'comment-input',
    imports: [FormsModule],
    templateUrl: './comment-input.html',
    styleUrl: './comment-input.css',
})
export class CommentInput {
    @Input() placeholder: string = 'Write a comment...';
    @Output() commentSubmitted = new EventEmitter<string>();

    commentText: string = '';

    updateText(event: Event) {
        this.commentText = (event.target as HTMLTextAreaElement).value;
    }

    onSubmit() {
        if (this.commentText?.trim()) {
            this.commentSubmitted.emit(this.commentText.trim());
            this.commentText = '';
        }
    }

    onKeyDown(event: KeyboardEvent): void {
        if ((event.ctrlKey || event.metaKey) && event.key === 'Enter') {
            event.preventDefault();
            this.onSubmit();
        }
    }
}
