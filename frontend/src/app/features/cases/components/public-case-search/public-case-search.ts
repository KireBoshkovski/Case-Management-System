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
    Subject,
    switchMap,
} from 'rxjs';
import { CaseDto } from '../../../../models/cases/case-dto.model';
import { PageResponse } from '../../../../models/page-response';
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
    ];

    cases: PublicCase[] = [];

    query: string = '';

    currentPage = 0;
    totalElements = 0;
    resultsPerPage = 10;

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
                sort: ['createdAt,desc'],
                query: query?.trim() || undefined,
            }),
        ),
        shareReplay({ bufferSize: 1, refCount: true }),
    );

    readonly cases$ = this.pageResponse$.pipe(
        map((res: PageResponse<PublicCase>) => res.content),
    );
    readonly totalElements$ = this.pageResponse$.pipe(
        map((res) => res.totalElements),
    );
    readonly currentPage$ = this.pageResponse$.pipe(map((res) => res.page));

    onSearch(q: string) {
        this.page$.next(0);
        this.query$.next(q);
    }

    onPageChange(nextPage: number) {
        this.page$.next(nextPage);
    }

    setPageSize(size: number) {
        this.size$.next(size);
        this.page$.next(0);
    }
}
