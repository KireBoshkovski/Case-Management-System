import {Component, inject, OnInit} from '@angular/core';
import {PatientsService} from '../../../../core/services/patients.service';
import {CaseService} from '../../../../core/services/case.service';
import {ActivatedRoute} from '@angular/router';
import {PatientDetailsModel} from '../../../../models/patient-details.model';
import {List} from '../../../../shared/components/list/list';
import {ColumnDef} from '../../../../models/columnDef';
import {CommonModule, DatePipe} from '@angular/common';
import {CaseDto} from '../../../../models/case.dto';
import {PageResponse} from '../../../../models/page-response.model';

@Component({
    selector: 'patient-details',
    imports: [CommonModule, List, DatePipe],
    templateUrl: './patient-details.html',
    styleUrl: './patient-details.css',
})
export class PatientDetails implements OnInit {
    patientService = inject(PatientsService);
    caseService = inject(CaseService);
    route = inject(ActivatedRoute);

    patient?: PatientDetailsModel;
    loading = true;

    cases: CaseDto[] = [];

    caseColumns: ColumnDef<CaseDto>[] = [
        {header: 'Case ID', field: 'id'},
        {header: 'Status', field: 'status'},
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
            next: (page: PageResponse<any>) => {//TODO
                this.cases = page.content; // <-- was assigning the whole page before
            },
            error: (err) => {
                console.error('Error fetching cases for patient:', err);
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
