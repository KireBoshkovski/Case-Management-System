import { OnInit, Component, inject } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { Case } from '../../../../models/cases/case.model';
import { ActivatedRoute } from '@angular/router';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';
import { PatientDetailsModel } from '../../../../models/patient-details.model';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { CaseDto } from '../../../../models/cases/case-dto.model';

@Component({
    selector: 'case-search',
    imports: [SearchBar, List, Pagination],
    templateUrl: './case-search.html',
    styleUrl: './case-search.css',
})
export class CaseSearch implements OnInit {
    service = inject(CaseService);
    route = inject(ActivatedRoute);

    cases: CaseDto[] = [];

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

    ngOnInit() {
        this.service.getCases().subscribe({
            next: (caseData) => {
                console.log('Fetched cases:', caseData);
                this.cases = caseData;
            },
            error: (err) => {
                console.error('Error fetching cases:', err);
            },
        });
    }
}
