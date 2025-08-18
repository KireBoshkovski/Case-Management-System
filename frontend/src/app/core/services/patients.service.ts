import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from '../../models/patient.model';
import { PatientDetailsModel } from '../../models/patient-details.model';
import { GetCasesOptions } from '../../models/cases-options';
import { PageResponse } from '../../models/page-response';

@Injectable({
    providedIn: 'root',
})
export class PatientsService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getPatients(options: GetCasesOptions): Observable<PageResponse<Patient>> {
        const {
            patientId,
            page = 1,
            size = 20,
            sort = ['lastName,desc'],
            query,
        } = options;

        let params = new HttpParams().set('page', page).set('size', size);

        sort.forEach((s) => (params = params.append('sort', s)));

        if (query && query.trim().length > 0)
            params = params.set('q', query.trim());

        console.log('Request with params:', params);

        return this.http.get<PageResponse<Patient>>(`${this.apiUrl}/patients`, {
            params,
        });
    }

    getPatientById(id: number): Observable<PatientDetailsModel> {
        return this.http.get<PatientDetailsModel>(
            `${this.apiUrl}/patients/${id}`,
        );
    }
}
