import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Examination } from '../../models/examination.model';

@Injectable({
    providedIn: 'root',
})
export class ExaminationService {
    private apiUrl = environment.apiUrl;

    http = inject(HttpClient);

    getExaminationById(id: number) {
        console.log('Requesting examination with id:', id);

        return this.http.get<Examination>(`${this.apiUrl}/examinations/${id}`);
    }
}
