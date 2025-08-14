import { Component, inject, OnInit } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { CaseDto } from '../../../../models/case.dto';
import {
    ReactiveFormsModule,
    FormBuilder,
    FormGroup,
    Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Case } from '../../../../models/cases/case.model';

@Component({
    selector: 'create-case',
    imports: [ReactiveFormsModule],
    templateUrl: './create-case.html',
    styleUrl: './create-case.css',
})
export class CreateCase implements OnInit {
    caseService = inject(CaseService);
    formBuilder = inject(FormBuilder);
    router = inject(Router);
    route = inject(ActivatedRoute);

    caseForm: FormGroup = this.formBuilder.group({
        bloodType: [''],
        allergies: [''],
        description: ['', Validators.required],
        treatmentPlan: [''],
        status: ['ACTIVE', Validators.required],
        patientId: ['', [Validators.required, Validators.min(1)]],
        doctorId: ['', [Validators.required, Validators.min(1)]],
        examinationsIds: [[]], // Start with empty array
    });

    isEditMode: boolean = false;
    caseId: number | undefined;

    ngOnInit() {
        this.caseId = Number(this.route.snapshot.paramMap.get('id'));

        this.isEditMode = !!this.caseId;

        if (this.isEditMode) {
            this.loadCaseForEditing(this.caseId);
        }
    }

    private loadCaseForEditing(caseId: number) {
        this.caseService.getCaseById(caseId).subscribe({
            next: (caseData) => {
                console.log('Loaded case for edit:', caseData);
                this.populateForm(caseData);
            },
            error: (err) => {
                console.error('Error loading case:', err);
            },
        });
    }

    private populateForm(caseData: Case) {
        this.caseForm.patchValue({
            bloodType: caseData.bloodType || '',
            allergies: caseData.allergies || '',
            description: caseData.description || '',
            treatmentPlan: caseData.treatmentPlan || '',
            status: caseData.status || 'ACTIVE',
            patientId: caseData.patient.id || caseData.patient?.id || '',
            doctorId: caseData.doctor.id || caseData.doctor?.id || '',
            examinationsIds:
                caseData.examinations?.map((exam) => exam.id) || [],
        });
    }

    onSubmit() {
        if (this.caseForm.valid) {
            const formValue = this.caseForm.value;
            const caseData: Partial<CaseDto> = {
                bloodType: formValue.bloodType || undefined,
                allergies: formValue.allergies || undefined,
                description: formValue.description,
                treatmentPlan: formValue.treatmentPlan || undefined,
                status: formValue.status,
                // patientId: Number(formValue.patientId), //TODO
                doctorId: Number(formValue.doctorId),
                // examinationsIds: formValue.examinationsIds || [], //TODO
            };

            if (this.isEditMode) {
                this.updateCase(caseData);
            } else {
                this.createCase(caseData);
            }
        } else {
            this.markFormGroupTouched();
        }
    }

    private createCase(caseData: Partial<CaseDto>) {
        this.caseService.createCase(caseData).subscribe({
            next: (createdCase) => {
                console.log('Case created successfully:', createdCase);
                this.router.navigate(['/cases', createdCase.id]);
            },
            error: (err) => {
                console.error('Error creating case:', err);
            },
        });
    }

    private updateCase(caseData: Partial<CaseDto>) {
        this.caseService.updateCase(this.caseId!, caseData).subscribe({
            next: (updatedCase) => {
                console.log('Case updated successfully:', updatedCase);
                this.router.navigate(['/cases', updatedCase.id]);
            },
            error: (err) => {
                console.error('Error updating case:', err);
            },
        });
    }

    private markFormGroupTouched() {
        Object.keys(this.caseForm.controls).forEach((key) => {
            const control = this.caseForm.get(key);
            control?.markAsTouched();
        });
    }

    getPageTitle(): string {
        return this.isEditMode ? 'Edit Case' : 'Create New Case';
    }

    getSubmitButtonText(): string {
        return this.isEditMode ? 'Update Case' : 'Create Case';
    }

    isFieldInvalid(fieldName: string): boolean {
        const field = this.caseForm.get(fieldName);
        return !!(field?.invalid && field?.touched);
    }

    getFieldError(fieldName: string): string {
        const field = this.caseForm.get(fieldName);
        if (field?.errors?.['required']) {
            return `${fieldName} is required`;
        }
        if (field?.errors?.['min']) {
            return `${fieldName} must be greater than 0`;
        }
        return '';
    }
}
