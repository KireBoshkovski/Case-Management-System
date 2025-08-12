// src/app/features/threads/thread-detail.ts
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ThreadService, ThreadResponse } from '../../../core/services/thread.service';
import { DiscussionService, DiscussionResponse } from '../../../core/services/discussion.service';


@Component({
    standalone: true,
    selector: 'app-thread-detail',
    imports: [CommonModule, FormsModule],
    templateUrl: './thread-detail.html',
})
export default class ThreadDetail implements OnInit {
    private route = inject(ActivatedRoute);
    private threadSvc = inject(ThreadService);
    private discSvc = inject(DiscussionService);

    thread?: ThreadResponse;

    discussions: DiscussionResponse[] = [];
    page = 0;
    size = 20;
    hasMore = true;

    // demo-only;
    doctorId = 1;

    newContent = '';
    newDiagnosis: string | null = null;
    newConfidence: number | null = null;

    ngOnInit(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
        this.threadSvc.getById(id).subscribe(t => {
            this.thread = t;
            this.fetchDiscussions(); // start after thread is known
        });
    }

    fetchDiscussions(): void {
        if (!this.thread) return;
        this.discSvc.listByThread(this.thread.id, this.page, this.size).subscribe(p => {
            this.discussions = [...this.discussions, ...p.content];
            this.hasMore = !p.last && (this.page + 1) < p.totalPages;
        });
    }

    loadMore(): void {
        if (!this.hasMore) return;
        this.page++;
        this.fetchDiscussions();
    }

    postDiscussion(): void {
        if (!this.thread || !this.newContent.trim()) return;
        this.discSvc.add({
            threadId: this.thread.id,
            doctorId: this.doctorId,
            content: this.newContent.trim(),
            diagnosisSuggestion: this.newDiagnosis || null,
            confidenceLevel: this.newConfidence ?? null,
        }).subscribe(d => {
            this.discussions = [d, ...this.discussions];
            this.newContent = '';
            this.newDiagnosis = null;
            this.newConfidence = null;
        });
    }

    fork(): void {
        if (!this.thread) return;
        this.threadSvc.fork(this.thread.id, 2).subscribe(forked => {
            alert(`Fork created: #${forked.id}`);
        });
    }

    close(): void {
        if (!this.thread) return;
        this.threadSvc.close(this.thread.id).subscribe(t => this.thread = t);
    }
}
