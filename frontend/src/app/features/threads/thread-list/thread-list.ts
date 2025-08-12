import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ThreadResponse, ThreadService} from '../../../core/services/thread.service';

@Component({
    standalone: true,
    selector: 'app-thread-list',
    imports: [CommonModule, RouterLink],
    templateUrl: `./thread-list.html`,
})
export default class ThreadList implements OnInit {
    private svc = inject(ThreadService);

    threads: ThreadResponse[] = [];
    page = 0;
    size = 12;
    hasMore = true;

    ngOnInit(): void {
        this.fetch();
    }

    fetch(): void {
        this.svc.getActive(this.page, this.size).subscribe(p => {
            this.threads = [...this.threads, ...p.content];
            this.hasMore = !p.last && (this.page + 1) < p.totalPages;
        });
    }

    loadMore(): void {
        if (!this.hasMore) return;
        this.page++;
        this.fetch();
    }
}
