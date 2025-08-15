import { Component, inject, OnInit } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { PublicCase } from '../../../../models/cases/public-case.model';
import { ActivatedRoute } from '@angular/router';
import { List } from '../../../../shared/components/list/list';

@Component({
    selector: 'public-case-details',
    imports: [],
    templateUrl: './public-case-details.html',
    styleUrl: './public-case-details.css',
})
export class PublicCaseDetails implements OnInit {
    caseService = inject(CaseService);

    route = inject(ActivatedRoute);

    caseData: PublicCase | undefined;

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
