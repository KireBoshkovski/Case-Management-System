import { Component, inject, OnInit } from '@angular/core';
import { DatePipe, CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { PatientsService } from '../../../../core/services/patients.service';
import { CaseService } from '../../../../core/services/case.service';

import { PatientDetailsModel } from '../../../../models/patient-details.model';
import { CaseDto } from '../../../../models/case.dto';
import { ColumnDef } from '../../../../models/columnDef';
import { List } from '../../../../shared/components/list/list';

@Component({
    selector: 'patient-details',
    standalone: true,
    imports: [CommonModule, List, DatePipe],
    templateUrl: './patient-details.html',
    styleUrl: './patient-details.css',
})
export class PatientDetails implements OnInit {
    private patientService = inject(PatientsService);
    private caseService = inject(CaseService);
    private route = inject(ActivatedRoute);

    patient?: PatientDetailsModel;
    loading = true;

    cases: CaseDto[] = [];

    caseColumns: ColumnDef<CaseDto>[] = [
        { header: 'Case ID', field: 'id' },
        { header: 'Status', field: 'status' },
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

    ngOnInit() {
        const patientId = Number(this.route.snapshot.paramMap.get('id'));

        this.patientService.getPatientById(patientId).subscribe({
            next: (details) => {
                this.patient = details;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error fetching patient details:', err);
                this.loading = false;
            },
        });

        this.caseService.getCasesByPatientIdPaged(patientId).subscribe({
            next: (page) => {
                this.cases = page.content; // <-- was assigning the whole page before
            },
            error: (err) => {
                console.error('Error fetching cases for patient:', err);
            },
        });
    }
}
