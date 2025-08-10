import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CaseService } from '../../../../core/services/case.service';
import { Case } from '../../../../models/case.model';
import { CaseStatus } from '../../../../models/case-status.enum';

@Component({
    selector: 'create-case',
    imports: [ReactiveFormsModule],
    templateUrl: './create-case.html',
    styleUrl: './create-case.css',
})
export class CreateCase {
    caseService = inject(CaseService);
    fb = inject(FormBuilder);

    caseForm = this.fb.group({
        bloodType: [''],
        allergies: [''],
        description: [''],
        treatmentPlan: [''],
        status: ['ACTIVE', Validators.required],
        patientId: [null, Validators.required],
        doctorId: [null, Validators.required],
    });

    onSubmit() {
        const newCase: Case = {
            bloodType: this.caseForm.value.bloodType ?? undefined,
            allergies: this.caseForm.value.allergies ?? undefined,
            description: this.caseForm.value.description ?? undefined,
            treatmentPlan: this.caseForm.value.treatmentPlan ?? undefined,
            status:
                (this.caseForm.value.status as CaseStatus) ?? CaseStatus.ACTIVE,
            patientId: this.caseForm.value.patientId! as number,
            doctorId: this.caseForm.value.doctorId! as number,
            examinations: [],
        };

        this.caseService.createCase(newCase).subscribe({
            next: (createdCase) => {
                console.log('Case created:', createdCase);
            },
            error: (err) => {
                console.error('Error creating case:', err);
            },
        });
    }
}
