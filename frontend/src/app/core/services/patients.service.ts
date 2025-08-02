import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from '../../models/patient.model';
import { PatientDetailsModel } from '../../models/patient-details.model';

@Injectable({
    providedIn: 'root',
})
export class PatientsService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getPatients(): Observable<Patient[]> {
        return this.http.get<Patient[]>(`${this.apiUrl}/patients`);
    }

    getPatientById(id: number): Observable<PatientDetailsModel> {
        return this.http.get<PatientDetailsModel>(
            `${this.apiUrl}/patients/${id}`
        );
    }
}
