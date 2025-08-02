import { Component, inject, OnInit } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { ActivatedRoute } from '@angular/router';
import { Case } from '../../../../models/case.model';
import { DatePipe, KeyValuePipe } from '@angular/common';

@Component({
    selector: 'case-detail',
    imports: [DatePipe, KeyValuePipe],
    templateUrl: './case-detail.html',
    styleUrl: './case-detail.css',
})
export class CaseDetail implements OnInit {
    service = inject(CaseService);
    route = inject(ActivatedRoute);

    caseId: number | null = null;
    caseData: Case | undefined;
    loading: boolean = true;

    ngOnInit(): void {
        this.caseId = Number(this.route.snapshot.paramMap.get('id'));

        console.log('Loading Case Detail');

        if (this.caseId) {
            this.service.getCaseById(this.caseId).subscribe({
                next: (caseData) => {
                    this.caseData = caseData;
                    this.loading = false;
                },
                error: (err) => {
                    console.error('Error fetching case:', err);
                    this.loading = false;
                },
            });
        } else {
            console.error('Invalid case ID');
            this.loading = false;
        }
    }

    parseVitalSigns(vitalSigns: string): any {
        try {
            return JSON.parse(vitalSigns);
        } catch (e) {
            return null;
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

    calculateAge(dateOfBirth: string): number {
        const today = new Date();
        const birthDate = new Date(dateOfBirth);
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();

        if (
            monthDiff < 0 ||
            (monthDiff === 0 && today.getDate() < birthDate.getDate())
        ) {
            age--;
        }

        return age;
    }
}
