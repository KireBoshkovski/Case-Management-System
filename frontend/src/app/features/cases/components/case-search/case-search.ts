import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, combineLatest, map, shareReplay, switchMap, Observable } from 'rxjs';

import { CaseService } from '../../../../core/services/case.service';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { List } from '../../../../shared/components/list/list';

import { ColumnDef } from '../../../../models/columnDef';
import { CaseDto } from '../../../../models/case.dto';
import { PageResponse } from '../../../../models/page-response.model';
import { Visibility } from '../../../../models/visibility.type';
import { PatientDetailsModel } from '../../../../models/patient-details.model';

@Component({
    selector: 'case-search',
    standalone: true,
    imports: [SearchBar, List, CommonModule],
    templateUrl: './case-search.html',
    styleUrls: ['./case-search.css'],
})
export class CaseSearch implements OnInit {
    private service = inject(CaseService);
    private route = inject(ActivatedRoute);

    private page$ = new BehaviorSubject<number>(0);
    private size$ = new BehaviorSubject<number>(10);
    private query$ = new BehaviorSubject<string>('');

    // derive visibility from route.data ('public' => 'PUBLIC', else 'ALL')
    readonly visibility$: Observable<Visibility> = this.route.data.pipe(
        map(d => (d['public'] ? 'PUBLIC' : 'ALL'))
    );

    readonly pageResponse$ = combineLatest([
        this.visibility$, this.page$, this.size$, this.query$
    ]).pipe(
        switchMap(([visibility, page, size, q]) =>
            this.service.getCases({
                visibility,
                page,
                size,
                sort: ['createdAt,desc'],
                query: q?.trim() || undefined,
            })
        ),
        shareReplay({ bufferSize: 1, refCount: true })
    );

    readonly cases$ = this.pageResponse$.pipe(map((res: PageResponse<CaseDto>) => res.content));
    readonly totalPages$ = this.pageResponse$.pipe(map(res => res.totalPages));
    readonly currentPage$ = this.pageResponse$.pipe(map(res => res.page));

    caseColumns: ColumnDef<CaseDto>[] = [
        {
            header: 'Patient',
            field: 'patient',
            formatter: (p: PatientDetailsModel) => `${p.firstName} ${p.lastName}`,
            idField: 'id',
        },
    ];

    ngOnInit(): void {
        // no-op; streams above drive fetching
    }

    onSearchChange(q: string) {
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
