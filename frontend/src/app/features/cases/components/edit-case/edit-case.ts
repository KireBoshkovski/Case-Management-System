import { Component, inject, OnInit } from '@angular/core';
import { Case } from '../../../../models/case.model';
import { CaseService } from '../../../../core/services/case.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import {
    FormBuilder,
    FormGroup,
    FormArray,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { PublicCase } from '../../../../models/public-case';
import { PublicExamination } from '../../../../models/public-examination';

@Component({
    selector: 'edit-case',
    imports: [DatePipe, ReactiveFormsModule],
    templateUrl: './edit-case.html',
    styleUrl: './edit-case.css',
})
export class EditCase implements OnInit {
    private caseService = inject(CaseService);
    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private fb = inject(FormBuilder);

    caseForm!: FormGroup;
    originalCase?: Case; // Add this property
    censoredCase?: Case; // Add this property
    loading = true;
    saving = false;
    errorMessage = '';

    ngOnInit(): void {
        const caseId = Number(this.route.snapshot.paramMap.get('id'));
        if (!caseId) {
            this.errorMessage = 'Invalid case ID';
            this.loading = false;
            return;
        }

        // First get the original case to extract patient info
        this.caseService.getCaseById(caseId).subscribe({
            next: (originalCase) => {
                this.originalCase = originalCase;

                // Then get the censored version
                this.caseService.censorCase(caseId).subscribe({
                    next: (censoredCase) => {
                        this.censoredCase = censoredCase;
                        this.initForm(censoredCase);
                        this.loading = false;
                    },
                    error: () => {
                        this.errorMessage =
                            'Failed to load censored case details.';
                        this.loading = false;
                    },
                });
            },
            error: () => {
                this.errorMessage = 'Failed to load original case details.';
                this.loading = false;
            },
        });
    }

    initForm(caseData: Case) {
        // Calculate patient age range and gender from original case
        const patientAgeRange = this.originalCase?.patientId?
            ? this.calculateAgeRange(this.originalCase.patient.dateOfBirth)
            : '';
        const patientGender = this.originalCase?.patient?.gender || '';

        this.caseForm = this.fb.group({
            description: [caseData.description || '', Validators.required],
            treatmentPlan: [caseData.treatmentPlan || ''],
            bloodType: [caseData.bloodType || ''],
            allergies: [caseData.allergies || ''],
            patientAgeRange: [{ value: patientAgeRange, disabled: true }], // Make readonly
            patientGender: [patientGender, Validators.required],
            status: [caseData.status || 'ACTIVE'],
            createdAt: [{ value: caseData.createdAt, disabled: true }],
            updatedAt: [{ value: caseData.updatedAt, disabled: true }],
            originalCaseId: [caseData.id], // Store original case ID
            examinations: this.fb.array([]), // Initialize examinations FormArray
        });

        // Initialize examinations form array
        this.initExaminationsFormArray();
    }

    initExaminationsFormArray() {
        const examinationsFormArray = this.caseForm.get(
            'examinations',
        ) as FormArray;

        if (this.censoredCase?.examinations) {
            this.censoredCase.examinations.forEach((exam, index) => {
                const originalExam =
                    this.originalCase?.examinations?.find(
                        (origExam) => origExam.id === exam.id,
                    ) || this.originalCase?.examinations?.[index];

                const examinationGroup = this.fb.group({
                    id: [exam.id],
                    originalExaminationId: [originalExam?.id || exam.id],
                    examinationType: [
                        exam.examinationType || '',
                        Validators.required,
                    ],
                    findings: [exam.findings || ''],
                    results: [exam.results || ''],
                    notes: [exam.notes || ''],
                    vitalSigns: [exam.vitalSigns || ''],
                    examinationDate: [exam.examinationDate],
                    examiningDoctorSpecialty: [
                        originalExam?.doctor?.specialization || '',
                    ],
                });

                examinationsFormArray.push(examinationGroup);
            });
        }
    }

    // Getter for examinations FormArray
    get examinationsFormArray(): FormArray {
        return this.caseForm.get('examinations') as FormArray;
    }

    // Get examination FormGroup at specific index
    getExaminationFormGroup(index: number): FormGroup {
        return this.examinationsFormArray.at(index) as FormGroup;
    }

    confirmPublish() {
        if (this.caseForm.invalid || !this.originalCase || !this.censoredCase) {
            this.errorMessage = 'Please fill in all required fields.';
            return;
        }

        this.saving = true;
        this.errorMessage = ''; // Clear previous errors

        const publicCase: PublicCase = {
            id: 0, // Will be generated by backend
            bloodType: this.caseForm.value.bloodType,
            allergies: this.caseForm.value.allergies,
            description: this.caseForm.value.description,
            treatmentPlan: this.caseForm.value.treatmentPlan,
            patientAgeRange: this.caseForm.get('patientAgeRange')?.value || '', // Get from disabled field
            patientGender: this.caseForm.value.patientGender,
            createdAt: this.originalCase.createdAt || new Date().toISOString(),
            updatedAt: new Date().toISOString(),
            publishedAt: new Date().toISOString(),
            examinations: this.createPublicExaminations(),
        };

        this.caseService
            .publishCase(this.originalCase.id!, publicCase)
            .subscribe({
                next: (response) => {
                    this.router.navigate(['/cases/public']);
                },
                error: (error) => {
                    console.error('Publishing failed:', error);
                    this.errorMessage =
                        'Failed to publish case. Please try again.';
                    this.saving = false;
                },
            });
    }

    private createPublicExaminations(): PublicExamination[] {
        const examinationsFormArray = this.caseForm.get(
            'examinations',
        ) as FormArray;

        if (!examinationsFormArray || examinationsFormArray.length === 0) {
            return [];
        }

        return examinationsFormArray.controls.map((control) => {
            const examValue = control.value;

            return {
                id: 0, // Will be generated by backend
                originalExaminationId: examValue.originalExaminationId || 0,
                examinationType: examValue.examinationType || 'Unknown',
                findings: examValue.findings || '',
                results: examValue.results || '',
                notes: examValue.notes || '',
                vitalSigns: examValue.vitalSigns || '',
                examinationDate: examValue.examinationDate,
                examiningDoctorSpecialty:
                    examValue.examiningDoctorSpecialty || 'Unknown',
                publishedAt: new Date().toISOString(),
            };
        });
    }

    calculateAgeRange(dateOfBirth: string): string {
        if (!dateOfBirth) return '';

        const birthDate = new Date(dateOfBirth);
        const today = new Date();

        // Handle case where birth month/day hasn't occurred yet this year
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();

        if (
            monthDiff < 0 ||
            (monthDiff === 0 && today.getDate() < birthDate.getDate())
        ) {
            age--;
        }

        const rangeStart = Math.floor(age / 5) * 5;
        const rangeEnd = rangeStart + 4;
        return `${rangeStart}-${rangeEnd}`;
    }

    // Helper method to navigate back
    goBack() {
        this.router.navigate(['/cases']);
    }
}
