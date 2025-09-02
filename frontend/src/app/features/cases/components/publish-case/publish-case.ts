import {Component, inject, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators,} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {CaseService} from '../../../../core/services/case.service';
import {Case} from '../../../../models/cases/case.model';
import {PublicCase} from '../../../../models/cases/public-case.model';
import {Examination} from '../../../../models/examination.model';

@Component({
    selector: 'publish-case',
    imports: [ReactiveFormsModule],
    templateUrl: './publish-case.html',
    styleUrl: './publish-case.css',
})
export class PublishCase implements OnInit {
    caseService = inject(CaseService);
    formBuilder = inject(FormBuilder);
    router = inject(Router);
    route = inject(ActivatedRoute);

    publishForm: FormGroup = this.formBuilder.group({
        bloodType: [''],
        allergies: [''],
        description: ['', Validators.required],
        treatmentPlan: [''],
        patientAgeRange: ['', Validators.required],
        patientGender: ['', Validators.required],
        examinations: this.formBuilder.array([]),
        createdAt: [new Date().toISOString(), Validators.required],
        updatedAt: [new Date().toISOString(), Validators.required],
    });

    caseId: number | undefined;
    originalCase: Case | null = null;
    censoredCase: Case | null = null;

    censoring = false;
    publishing = false;
    errorMessage = '';

    ngOnInit() {
        this.caseId = Number(this.route.snapshot.paramMap.get('id'));
        if (this.caseId) {
            this.loadOriginalCase();
        } else {
            console.error('No case ID provided in the route.');
        }
    }

    loadOriginalCase() {
        if (!this.caseId) return;

        this.caseService.getCaseById(this.caseId).subscribe({
            next: (caseData) => {
                console.log('Original case loaded:', caseData);
                this.originalCase = caseData;
                this.populateForm(caseData);
            },
            error: (err) => {
                console.error('Error loading original case:', err);
                this.errorMessage = 'Failed to load case data';
            },
        });
    }

    populateExaminations(examinations: Examination[]) {
        const examinationsArray = this.getExaminationsFormArray();

        // Clear existing form array
        examinationsArray.clear();

        // Add examination form groups
        examinations.forEach((exam) => {
            examinationsArray.push(this.createExaminationFormGroup(exam));
        });
    }

    getExaminationsFormArray(): FormArray {
        return this.publishForm.get('examinations') as FormArray;
    }

    addExamination() {
        const examinationsArray = this.getExaminationsFormArray();
        examinationsArray.push(this.createExaminationFormGroup());
    }

    removeExamination(index: number) {
        const examinationsArray = this.getExaminationsFormArray();
        examinationsArray.removeAt(index);
    }

    requestCensoring() {
        if (!this.caseId) return;

        this.censoring = true;
        this.errorMessage = '';

        this.caseService.censorCase(this.caseId).subscribe({
            next: (censoredData) => {
                console.log('Case censored:', censoredData);
                this.censoredCase = censoredData;
                this.populateFormWithCensoredData(censoredData);
                this.censoring = false;
            },
            error: (err) => {
                console.error('Error censoring case:', err);
                this.errorMessage =
                    'Failed to censor case content. Please try again.';
                this.censoring = false;
            },
        });
    }

    publishCase() {
        if (this.publishForm.valid && this.caseId) {
            this.publishing = true;
            this.errorMessage = '';

            const formValue = this.publishForm.value;
            const publicCase: Partial<PublicCase> = {
                bloodType: formValue.bloodType || undefined,
                allergies: formValue.allergies || undefined,
                description: formValue.description,
                treatmentPlan: formValue.treatmentPlan || undefined,
                patientAgeRange: formValue.patientAgeRange,
                patientGender: formValue.patientGender,
                createdAt: formValue.createdAt,
                updatedAt: formValue.updatedAt,
                examinations: formValue.examinations.map((exam: any) => ({
                    originalExaminationId: exam.id,
                    examinationType: exam.examinationType,
                    examinationDate: exam.examinationDate,
                    findings: exam.findings,
                    results: exam.results,
                    notes: exam.notes,
                    vitalSigns: exam.vitalSigns,
                })),
            };

            console.log('Publishing case with data:', publicCase);

            this.caseService
                .publishCase(this.caseId, publicCase as PublicCase)
                .subscribe({
                    next: (data) => {
                        console.log('Case published successfully');
                        this.publishing = false;
                        // Navigate to public cases or back to case detail
                        this.router.navigate(['/public', data.id]);
                    },
                    error: (err) => {
                        console.error('Error publishing case:', err);
                        this.errorMessage =
                            'Failed to publish case. Please try again.';
                        this.publishing = false;
                    },
                });
        } else {
            this.markFormGroupTouched();
        }
    }

    isFieldInvalid(fieldName: string): boolean {
        const field = this.publishForm.get(fieldName);
        return !!(field?.invalid && field?.touched);
    }

    getFieldError(fieldName: string): string {
        const field = this.publishForm.get(fieldName);
        if (field?.errors?.['required']) {
            return `${fieldName} is required`;
        }
        return '';
    }

    hasCensoredData(): boolean {
        return this.censoredCase !== null;
    }

    private populateForm(caseData: Case) {
        this.publishForm.patchValue({
            bloodType: caseData.bloodType || '',
            allergies: caseData.allergies || '',
            description: caseData.description || '',
            treatmentPlan: caseData.treatmentPlan || '',
            patientGender: caseData.patient.gender || 'Select a gender',
            createdAt: caseData.createdAt,
            updatedAt: caseData.updatedAt,
        });

        const ageRange = this.deriveAgeRange(caseData.patient);
        this.publishForm.patchValue({
            patientAgeRange: ageRange,
            patientGender: caseData.patient.gender || '',
        });

        this.populateExaminations(caseData.examinations || []);
    }

    private createExaminationFormGroup(examination?: Examination): FormGroup {
        return this.formBuilder.group({
            id: [examination?.id || null],
            caseId: [examination?.caseId || null],
            examinationType: [
                examination?.examinationType || '',
                Validators.required,
            ],
            examinationDate: [
                examination?.examinationDate || '',
                Validators.required,
            ],
            findings: [examination?.findings || ''],
            results: [examination?.results || ''],
            notes: [examination?.notes || ''],
            vitalSigns: [examination?.vitalSigns || ''],
        });
    }

    private populateFormWithCensoredData(censoredCase: Case) {
        this.publishForm.patchValue({
            bloodType: censoredCase.bloodType || '',
            allergies: censoredCase.allergies || '',
            description: censoredCase.description || '',
            treatmentPlan: censoredCase.treatmentPlan || '',
        });

        if (censoredCase.patient) {
            const ageRange = this.deriveAgeRange(censoredCase.patient);
            this.publishForm.patchValue({
                patientAgeRange: ageRange,
                patientGender: censoredCase.patient.gender || '',
            });
        }

        this.populateExaminations(censoredCase.examinations || []);
    }

    private deriveAgeRange(patient: any): string {
        if (!patient.dateOfBirth) return '';

        const birthDate = new Date(patient.dateOfBirth);
        const age = Math.floor(
            (Date.now() - birthDate.getTime()) / (365.25 * 24 * 60 * 60 * 1000),
        );

        if (age < 18) return 'Under 18';
        if (age < 30) return '18-29';
        if (age < 45) return '30-44';
        if (age < 60) return '45-59';
        if (age < 75) return '60-74';
        return '75+';
    }

    private markFormGroupTouched() {
        Object.keys(this.publishForm.controls).forEach((key) => {
            const control = this.publishForm.get(key);
            control?.markAsTouched();
        });
    }
}
