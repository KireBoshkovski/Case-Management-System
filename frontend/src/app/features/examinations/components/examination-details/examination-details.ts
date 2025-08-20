import {Component, inject, OnInit} from '@angular/core';
import {ExaminationService} from '../../../../core/services/examination.service';
import {ActivatedRoute} from '@angular/router';
import {Examination} from '../../../../models/examination.model';

@Component({
    selector: 'examination-details',
    imports: [],
    templateUrl: './examination-details.html',
    styleUrl: './examination-details.css',
})
export class ExaminationDetails implements OnInit {
    examinationService = inject(ExaminationService);
    route = inject(ActivatedRoute);

    examination: Examination | undefined;

    ngOnInit(): void {
        const id = +this.route.snapshot.paramMap.get('id')!;

        this.examinationService.getExaminationById(id).subscribe({
            next: (response) => {
                this.examination = response;
            },
        });
    }

    formatDate(dateStr: string): string {
        return new Date(dateStr).toLocaleDateString();
    }
}
