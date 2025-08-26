import { Component, inject } from '@angular/core';
import { PublicCase } from '../../../../models/cases/public-case.model';
import { ActivatedRoute } from '@angular/router';
import { CaseService } from '../../../../core/services/case.service';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { List } from '../../../../shared/components/list/list';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { ColumnDef } from '../../../../models/columnDef';
import {
    BehaviorSubject,
    combineLatest,
    debounceTime,
    distinctUntilChanged,
    map,
    shareReplay,
    switchMap,
} from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'public-case-search',
    imports: [SearchBar, List, Pagination, AsyncPipe],
    templateUrl: './public-case-search.html',
    styleUrl: './public-case-search.css',
})
export class PublicCaseSearch {
    service = inject(CaseService);
    route = inject(ActivatedRoute);

    caseColumns: ColumnDef<PublicCase>[] = [
        { header: 'Case ID', field: 'id' },
        {
            header: 'Published At',
            field: 'publishedAt',
            formatter: (date: string) => new Date(date).toLocaleDateString(),
        },
        {
            header: 'Views',
            field: 'viewsCount',
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
            this.service.getPublicCases({
                page,
                size,
                sort: ['publishedAt,desc'],
                query: query?.trim() || undefined,
            }),
        ),
        shareReplay({ bufferSize: 1, refCount: true }),
    );
    readonly cases$ = this.pageResponse$.pipe(map((res) => res.content));
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
