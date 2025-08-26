import { Component, inject, OnInit } from '@angular/core';
import { CaseService } from '../../../../core/services/case.service';
import { CaseDto } from '../../../../models/cases/case.dto';
import {
    FormBuilder,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Case } from '../../../../models/cases/case.model';
import { ToastrService } from 'ngx-toastr';

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
    toastService = inject(ToastrService);

    caseForm: FormGroup = this.formBuilder.group({
        bloodType: [''],
        allergies: [''],
        description: ['', Validators.required],
        treatmentPlan: [''],
        status: ['ACTIVE', Validators.required],
        patientId: ['', [Validators.required, Validators.min(1)]],
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

    onSubmit() {
        if (this.caseForm.valid) {
            const v = this.caseForm.value;
            const caseData: Partial<CaseDto> = {
                bloodType: v.bloodType || undefined,
                allergies: v.allergies || undefined,
                description: v.description,
                treatmentPlan: v.treatmentPlan || undefined,
                status: v.status,
                patientId: Number(v.patientId),
                examinationsIds: (v.examinationsIds as number[]) || [],
            };

            this.isEditMode
                ? this.updateCase(caseData)
                : this.createCase(caseData);
        } else {
            this.markFormGroupTouched();
        }
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

    private loadCaseForEditing(caseId: number) {
        this.caseService.getCaseById(caseId).subscribe({
            next: (caseData) => {
                console.log('Loaded case for edit:', caseData);
                this.populateForm(caseData);
            },
            error: () => {
                this.toastService.error('Error loading case for editing.');
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
            patientId: caseData.patient?.id ?? '',
            examinationsIds: caseData.examinations?.map((e) => e.id) ?? [],
        });
    }

    private createCase(caseData: Partial<CaseDto>) {
        this.caseService.createCase(caseData).subscribe({
            next: (createdCase) => {
                this.toastService.success('Case created successfully!');
                this.router.navigate(['/cases', createdCase.id]);
            },
            error: (err) => {
                this.toastService.error(
                    err.error.error,
                    'Error creating case.',
                );
            },
        });
    }

    private updateCase(caseData: Partial<CaseDto>) {
        this.caseService.updateCase(this.caseId!, caseData).subscribe({
            next: (updatedCase) => {
                this.toastService.success('Case updated successfully!');
                this.router.navigate(['/cases', updatedCase.id]);
            },
            error: (err) => {
                this.toastService.error(
                    err.error.error,
                    'Error updating case.',
                );
            },
        });
    }

    private markFormGroupTouched() {
        Object.keys(this.caseForm.controls).forEach((key) => {
            const control = this.caseForm.get(key);
            control?.markAsTouched();
        });
    }
}
