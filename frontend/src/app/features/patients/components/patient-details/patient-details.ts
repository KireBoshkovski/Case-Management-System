import { Component, inject, OnInit } from '@angular/core';
import { PatientsService } from '../../../../core/services/patients.service';
import { CaseService } from '../../../../core/services/case.service';
import { ActivatedRoute } from '@angular/router';
import { PatientDetailsModel } from '../../../../models/patient-details.model';
import { Case } from '../../../../models/case.model';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'patient-details',
    imports: [List, DatePipe],
    templateUrl: './patient-details.html',
    styleUrl: './patient-details.css',
})
export class PatientDetails implements OnInit {
    patientService = inject(PatientsService);
    caseService = inject(CaseService);
    route = inject(ActivatedRoute);

    patient: PatientDetailsModel | undefined;
    loading: boolean = true;
    cases: Case[] = [];

    caseColumns: ColumnDef<Case>[] = [
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

    ngOnInit() {
        const patientId = Number(this.route.snapshot.paramMap.get('id'));

        console.log('Loading Patient Details');

        this.patientService.getPatientById(patientId).subscribe({
            next: (patientDetails) => {
                this.patient = patientDetails;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error fetching patient details:', err);
                this.loading = false;
            },
        });

        //        this.caseService.getCasesByPatientId(patientId).subscribe({
        //          next: (cases) => {
        //            this.cases = cases;
        //      },
        //    error: (err) => {
        //      console.error('Error fetching cases for patient:', err);
        //},
        // });
    }
}
