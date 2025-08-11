import { Component, inject, OnInit } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { ActivatedRoute } from '@angular/router';
import { Case } from '../../../../models/cases/case.model';
import { Doctor } from '../../../../models/doctor.model';
import { Patient } from '../../../../models/patient.model';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';
import { Examination } from '../../../../models/examination.model';
import { ForkListItem } from '../../../../models/forks/fork-list-item.model';

@Component({
    selector: 'case-detail',
    imports: [List],
    templateUrl: './case-detail.html',
    styleUrl: './case-detail.css',
})
export class CaseDetail implements OnInit {
    service = inject(CaseService);
    route = inject(ActivatedRoute);

    caseData: Case | undefined;
    doctorData: Doctor | undefined;
    patientData: Patient | undefined;

    examinationColumns: ColumnDef<Examination>[] = [
        {
            header: 'Examination ID',
            field: 'id',
        },
        { header: 'Examination Type', field: 'examinationType' },
        {
            header: 'Examination Date',
            field: 'examinationDate',
            formatter: (date: string) => new Date(date).toLocaleDateString(),
        },
    ];

    ngOnInit(): void {
        const caseId = Number(this.route.snapshot.paramMap.get('id'));
        if (caseId) {
            this.service.getCaseById(caseId).subscribe({
                next: (caseData) => {
                    this.caseData = caseData;
                },
                error: (err) => {
                    console.error('Error fetching case:', err);
                },
            });
        } else {
            console.error('Invalid case ID');
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
}
