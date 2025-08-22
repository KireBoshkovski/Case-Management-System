import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {environment} from '../../../environments/environments';
import {Examination} from '../../models/examination.model';

@Injectable({
    providedIn: 'root',
})
export class ExaminationService {
    http = inject(HttpClient);
    private apiUrl = environment.apiUrl;

    getExaminationById(id: number) {
        console.log('Requesting examination with id:', id);

        return this.http.get<Examination>(`${this.apiUrl}/examinations/${id}`);
    }
}
