import { Component, inject, OnInit } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { PublicCase } from '../../../../models/cases/public-case.model';
import { ActivatedRoute } from '@angular/router';
import { ForkListItem } from '../../../../models/forks/fork-list-item.model';
import { ColumnDef } from '../../../../models/columnDef';
import { ForkService } from '../../../../core/services/fork.service';
import { List } from '../../../../shared/components/list/list';
import { Examination } from '../../../../models/examination.model';

@Component({
    selector: 'public-case-details',
    imports: [List],
    templateUrl: './public-case-details.html',
    styleUrl: './public-case-details.css',
})
export class PublicCaseDetails implements OnInit {
    caseService = inject(CaseService);
    forkService = inject(ForkService);

    route = inject(ActivatedRoute);

    caseData: PublicCase | undefined;

    forks: ForkListItem[] = [];
    forkColumns: ColumnDef<ForkListItem>[] = [
        { field: 'id', header: 'ID' },
        { field: 'title', header: 'Title' },
        { field: 'originId', header: 'Origin ID' },
        { field: 'editorId', header: 'Editor ID' },
    ];

    ngOnInit() {
        const caseId = Number(this.route.snapshot.paramMap.get('id'));

        if (caseId) {
            this.caseService.getPublicCaseById(caseId).subscribe({
                next: (caseData) => {
                    console.log('Public Case Data:', caseData);
                    this.caseData = caseData;
                },
                error: (err) => {
                    console.error('Error fetching public case:', err);
                },
            });
        } else {
            console.error('Invalid case ID');
        }

        this.forkService.getForksByCaseId(caseId).subscribe({
            next: (forks) => {
                console.log('Forks for Case ID:', caseId, forks);
                this.forks = forks;
            },
            error: (err) => {
                console.error('Error fetching forks for case:', err);
            },
        });
    }

    formatDate(dateString: string): string {
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    }
}

