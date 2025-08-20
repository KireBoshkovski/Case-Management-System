import { Component, inject } from '@angular/core';
import { Patient } from '../../../../models/patient.model';
import { PatientsService } from '../../../../core/services/patients.service';
import { ActivatedRoute } from '@angular/router';
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
    selector: 'patient-search',
    imports: [SearchBar, List, Pagination, AsyncPipe],
    templateUrl: './patient-search.html',
    styleUrl: './patient-search.css',
})
export class PatientSearch {
    service = inject(PatientsService);
    route = inject(ActivatedRoute);

    patientColumns: ColumnDef<Patient>[] = [
        { header: 'Patient ID', field: 'id' },
        { header: 'First Name', field: 'firstName' },
        { header: 'Last Name', field: 'lastName' },
        { header: 'Email', field: 'email' },
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
            this.service.getPatients({
                page,
                size,
                query: query?.trim() || undefined,
            }),
        ),
        shareReplay({ bufferSize: 1, refCount: true }),
    );
    readonly patients$ = this.pageResponse$.pipe(map((res) => res.content));
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
