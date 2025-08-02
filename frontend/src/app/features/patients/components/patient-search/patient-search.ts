import { Component, inject, OnInit } from '@angular/core';
import { Patient } from '../../../../models/patient.model';
import { PatientsService } from '../../../../core/services/patients.service';
import { ActivatedRoute } from '@angular/router';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';

@Component({
    selector: 'patient-search',
    imports: [SearchBar, List, Pagination],
    templateUrl: './patient-search.html',
    styleUrl: './patient-search.css',
})
export class PatientSearch implements OnInit {
    service = inject(PatientsService);
    route = inject(ActivatedRoute);

    patients: Patient[] = [];
    patientColumns: ColumnDef<Patient>[] = [
        { header: 'Patient ID', field: 'id' },
        { header: 'First Name', field: 'firstName' },
        { header: 'Last Name', field: 'lastName' },
        { header: 'Email', field: 'email' },
    ];

    ngOnInit(): void {
        console.log('Loading Case Search');

        this.service.getPatients().subscribe({
            next: (patientData) => {
                this.patients = patientData;
            },
            error: (err) => {
                console.error('Error fetching patients:', err);
            },
        });
    }
}
