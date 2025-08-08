import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {BehaviorSubject, combineLatest, map, shareReplay, switchMap} from 'rxjs';

import {CaseService} from '../../../../core/services/case.service';
import {SearchBar} from '../../../../shared/components/search-bar/search-bar';
import {List} from '../../../../shared/components/list/list';

import {ColumnDef} from '../../../../models/columnDef';
import {PatientDetailsModel} from '../../../../models/patient-details.model';
import {CaseDto} from '../../../../models/case.dto';
import {PageResponse} from '../../../../models/page-response.model';
import {Visibility} from '../../../../models/visibility.type';

@Component({
    selector: 'case-search',
    standalone: true,
    imports: [CommonModule, SearchBar, List],
    templateUrl: './case-search.html',
    styleUrls: ['./case-search.css'],
})
export class CaseSearch implements OnInit {
    private service = inject(CaseService);
    private route = inject(ActivatedRoute);

    private page$ = new BehaviorSubject<number>(0);
    private size$ = new BehaviorSubject<number>(10);

    readonly visibility$ = this.route.data.pipe<Visibility>(
        map(d => (d['public'] ? 'PUBLIC' : 'ALL'))
    );

    readonly pageResponse$ = combineLatest([this.visibility$, this.page$, this.size$]).pipe(
        switchMap(([visibility, page, size]) =>
            this.service.getCases({
                visibility,
                page,
                size,
                sort: ['createdAt,desc'],
            })
        ),
        shareReplay({bufferSize: 1, refCount: true})
    );

    readonly cases$ = this.pageResponse$.pipe(map((res: PageResponse<CaseDto>) => res.content));
    readonly totalPages$ = this.pageResponse$.pipe(map(res => res.totalPages));
    readonly currentPage$ = this.pageResponse$.pipe(map(res => res.page));

    readonly caseColumns: ColumnDef<CaseDto>[] = [
        {
            header: 'Patient',
            field: 'patient',
            formatter: (p: PatientDetailsModel) => `${p.firstName} ${p.lastName}`,
            idField: 'id',
        },
        {header: 'Case ID', field: 'id'},
        {header: 'Status', field: 'status'},
        {
            header: 'Creation Date',
            field: 'createdAt',
            formatter: (iso: string) => new Date(iso).toLocaleDateString(),
        },
        {
            header: 'Last Update Date',
            field: 'updatedAt',
            formatter: (iso: string) => new Date(iso).toLocaleDateString(),
        },
    ];

    ngOnInit(): void {

    }

    onPageChange(nextPage: number) {
        this.page$.next(nextPage);
    }

    setPageSize(size: number) {
        this.size$.next(size);
        this.page$.next(0);
    }
}
