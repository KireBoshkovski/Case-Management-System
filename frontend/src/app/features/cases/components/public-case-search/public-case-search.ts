import { Component, inject, OnInit } from '@angular/core';
import { PublicCase } from '../../../../models/cases/public-case.model';
import { ActivatedRoute } from '@angular/router';
import { CaseService } from '../../../../core/services/case.service';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { List } from '../../../../shared/components/list/list';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { ColumnDef } from '../../../../models/columnDef';

@Component({
    selector: 'public-case-search',
    imports: [SearchBar, List, Pagination],
    templateUrl: './public-case-search.html',
    styleUrl: './public-case-search.css',
})
export class PublicCaseSearch implements OnInit {
    service = inject(CaseService);
    route = inject(ActivatedRoute);

    cases: PublicCase[] = [];

    caseColumns: ColumnDef<PublicCase>[] = [
        { header: 'Case ID', field: 'id' },
        {
            header: 'Published At',
            field: 'publishedAt',
            formatter: (date: string) => new Date(date).toLocaleDateString(),
        },
    ];

    ngOnInit() {
        this.service.getPublicCases().subscribe({
            next: (caseData) => {
                this.cases = caseData;
            },
            error: (err) => {
                console.error('Error fetching public cases:', err);
            },
        });
    }
}
