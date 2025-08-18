import { Component, inject } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { ActivatedRoute } from '@angular/router';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { CaseDto } from '../../../../models/cases/case.dto';
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
    selector: 'case-search',
    imports: [SearchBar, List, Pagination, AsyncPipe],
    templateUrl: './case-search.html',
    styleUrl: './case-search.css',
})
export class CaseSearch {
    service = inject(CaseService);
    route = inject(ActivatedRoute);

    caseColumns: ColumnDef<CaseDto>[] = [
        { header: 'Case ID', field: 'id' },
        {
            header: 'Patient',
            field: 'patientId',
        },
        { header: 'Status', field: 'status' },
        {
            header: 'Creation Date',
            field: 'createdAt',
            formatter: (date: string) => new Date(date).toLocaleDateString(),
        },
        {
            header: 'Last Update Date',
            field: 'updatedAt',
            formatter: (date: string) => new Date(date).toLocaleDateString(),
        },
    ];

    private page$ = new BehaviorSubject<number>(1);
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
            this.service.getCases({
                page,
                size,
                sort: ['createdAt,desc'],
                query: query?.trim() || undefined,
            }),
        ),
        shareReplay({ bufferSize: 1, refCount: true }),
    );

    readonly cases$ = this.pageResponse$.pipe(map((res) => res.content));
    readonly pageInfo$ = this.pageResponse$.pipe(map((res) => res.page));

    onSearch(q: string) {
        this.page$.next(1);
        this.query$.next(q);
    }

    onPageChange(nextPage: number) {
        this.page$.next(nextPage - 1);
    }

    setPageSize(size: number) {
        this.size$.next(size);
        this.page$.next(1);
    }
}
