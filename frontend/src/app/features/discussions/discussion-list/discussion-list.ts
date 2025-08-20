import { Component, inject } from '@angular/core';
import { DiscussionService } from '../../../core/services/discussion.service';
import { ColumnDef } from '../../../models/columnDef';
import { DiscussionDto } from '../../../models/discussions/discussion-dto';
import {
    BehaviorSubject,
    combineLatest,
    debounceTime,
    distinctUntilChanged,
    map,
    shareReplay,
    switchMap,
} from 'rxjs';
import { SearchBar } from '../../../shared/components/search-bar/search-bar';
import { Pagination } from '../../../shared/components/pagination/pagination';
import { List } from '../../../shared/components/list/list';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'discussion-list',
    imports: [SearchBar, Pagination, List, AsyncPipe],
    templateUrl: './discussion-list.html',
    styleUrl: './discussion-list.css',
})
export class DiscussionList {
    service = inject(DiscussionService);

    discussionColumns: ColumnDef<DiscussionDto>[] = [
        { header: 'Title', field: 'title' },
        {
            header: 'Created At',
            field: 'createdAt',
            formatter: (date: string) => new Date(date).toLocaleDateString(),
        },
        {
            header: 'Replies',
            field: 'commentsCount',
        },
    ];

    private page$ = new BehaviorSubject<number>(0);
    private size$ = new BehaviorSubject<number>(10);
    private query$ = new BehaviorSubject<string>('');

    readonly pageResponse$ = combineLatest([
        this.page$,
        this.size$,
        this.query$,
    ]).pipe(
        debounceTime(400),
        distinctUntilChanged(),
        switchMap(([page, size, query]) =>
            this.service.getDiscussions({
                page,
                size,
                sort: ['createdAt,desc'],
                query: query?.trim() || undefined,
            }),
        ),
        shareReplay({ bufferSize: 1, refCount: true }),
    );

    readonly discussions$ = this.pageResponse$.pipe(map((res) => res.content));
    readonly pageInfo$ = this.pageResponse$.pipe(map((res) => res.page));

    onSearch(q: string) {
        this.page$.next(0);
        this.query$.next(q);
    }

    onPageChange(nextPage: number) {
        this.page$.next(nextPage - 1);
    }

    setPageSize(size: number) {
        this.size$.next(size);
        this.page$.next(0);
    }
}
