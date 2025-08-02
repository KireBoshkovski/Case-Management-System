import { OnInit, Component, inject } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { Case } from '../../../../models/case.model';
import { ActivatedRoute } from '@angular/router';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';
import { Patient } from '../../../../models/patient.model';
import { Pagination } from '../../../../shared/components/pagination/pagination';

@Component({
    selector: 'case-search',
    imports: [SearchBar, List, Pagination],
    templateUrl: './case-search.html',
    styleUrl: './case-search.css',
})
export class CaseSearch implements OnInit {
    cases: Case[] = [];

    caseColumns: ColumnDef<Case>[] = [
        {
            header: 'Patient',
            field: 'patient',
            formatter: (patient: Patient) =>
                `${patient.firstName} ${patient.lastName}`,
            idField: 'id',
        },
        { header: 'Case ID', field: 'id' },
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

    service = inject(CaseService);
    route = inject(ActivatedRoute);

    ngOnInit(): void {
        const isPublic: boolean = this.route.snapshot.data['public'] || false;

        if (isPublic) {
            console.log('Fetching public cases');
            this.service.getPublicCases().subscribe({
                next: (caseData) => {
                    this.cases = caseData;
                    console.log('Fetched public cases:', this.cases);
                },
                error: (err) => {
                    console.error('Error fetching public cases:', err);
                },
            });
        } else {
            this.service.getCases().subscribe({
                next: (caseData) => {
                    this.cases = caseData;
                    console.log('Fetched cases:', this.cases);
                },
                error: (err) => {
                    console.error('Error fetching cases:', err);
                },
            });
        }
    }
}
