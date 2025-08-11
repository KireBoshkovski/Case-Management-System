import { Component, inject, OnInit } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ForkService } from '../../../../core/services/fork.service';
import { Fork } from '../../../../models/forks/fork';

@Component({
    selector: 'create-fork',
    imports: [ReactiveFormsModule],
    templateUrl: './create-fork.html',
    styleUrl: './create-fork.css',
})
export class CreateFork implements OnInit {
    forkService = inject(ForkService);
    route = inject(ActivatedRoute);
    router = inject(Router);
    fb = inject(FormBuilder);

    forkForm: FormGroup;
    isEditMode = false;
    forkId: number | null = null;

    constructor() {
        this.forkForm = this.fb.group({
            title: [
                '',
                [
                    Validators.required,
                    Validators.minLength(3),
                    Validators.maxLength(200),
                ],
            ],
            description: ['', [Validators.required, Validators.minLength(10)]],
            alternativeDiagnosis: [
                '',
                [Validators.required, Validators.minLength(10)],
            ],
            alternativeTreatment: [
                '',
                [Validators.required, Validators.minLength(10)],
            ],
            analysisNotes: [
                '',
                [Validators.required, Validators.minLength(10)],
            ],
            recommendations: [
                '',
                [Validators.required, Validators.minLength(10)],
            ],
            originId: ['', [Validators.required, Validators.min(1)]],
        });
    }

    ngOnInit() {
        this.forkId = Number(this.route.snapshot.params['id']);

        if (this.forkId) {
            this.isEditMode = true;
            this.loadForkForEdit();
        }

        const originId = this.route.snapshot.queryParams['originId'];
        if (originId && !this.isEditMode) {
            this.forkForm.patchValue({ originId: parseInt(originId) });
        }
    }

    private loadForkForEdit() {
        this.forkService.getFork(this.forkId!!).subscribe({
            next: (fork) => {
                this.forkForm.patchValue({
                    title: fork.title,
                    description: fork.description,
                    alternativeDiagnosis: fork.alternativeDiagnosis,
                    alternativeTreatment: fork.alternativeTreatment,
                    analysisNotes: fork.analysisNotes,
                    recommendations: fork.recommendations,
                    originId: fork.originId,
                });
            },
            error: (error) => {
                console.error('Error loading fork for edit:', error);
            },
        });
    }

    onSubmit() {
        if (this.forkForm.invalid) {
            this.markFormGroupTouched();
            return;
        }

        const formValue = this.forkForm.value;

        if (this.isEditMode && this.forkId) {
            const updatedFork: Fork = {
                id: this.forkId,
                ...formValue,
                editorId: 0,
            };

            this.forkService.updateFork(this.forkId, updatedFork).subscribe({
                next: () => {
                    this.router.navigate(['/forks', this.forkId]);
                },
                error: (error) => {
                    console.error('Error updating fork:', error);
                },
            });
        } else {
            // Create new fork
            const newFork: Omit<Fork, 'id'> = {
                ...formValue,
                editorId: 0, // This should be set by the backend based on authenticated user
            };

            this.forkService.createFork(newFork).subscribe({
                next: (createdFork) => {
                    this.router.navigate(['/forks', createdFork.id]);
                },
                error: (error) => {
                    console.error('Error creating fork:', error);
                },
            });
        }
    }

    onCancel() {
        if (this.isEditMode && this.forkId) {
            this.router.navigate(['/forks', this.forkId]);
        } else {
            this.router.navigate(['/cases']); // or wherever you want to navigate back to
        }
    }

    private markFormGroupTouched() {
        Object.keys(this.forkForm.controls).forEach((key) => {
            const control = this.forkForm.get(key);
            control?.markAsTouched();
        });
    }

    getFieldError(fieldName: string): string | null {
        const field = this.forkForm.get(fieldName);
        if (field?.touched && field?.errors) {
            if (field.errors['required'])
                return `${this.getFieldDisplayName(fieldName)} is required`;
            if (field.errors['minlength'])
                return `${this.getFieldDisplayName(fieldName)} must be at least ${field.errors['minlength'].requiredLength} characters`;
            if (field.errors['maxlength'])
                return `${this.getFieldDisplayName(fieldName)} must be less than ${field.errors['maxlength'].requiredLength} characters`;
            if (field.errors['min'])
                return `${this.getFieldDisplayName(fieldName)} must be a positive number`;
        }
        return null;
    }

    isFieldInvalid(fieldName: string): boolean {
        const field = this.forkForm.get(fieldName);
        return !!(field?.touched && field?.errors);
    }

    private getFieldDisplayName(fieldName: string): string {
        const displayNames: { [key: string]: string } = {
            title: 'Title',
            description: 'Description',
            alternativeDiagnosis: 'Alternative Diagnosis',
            alternativeTreatment: 'Alternative Treatment',
            analysisNotes: 'Analysis Notes',
            recommendations: 'Recommendations',
            originId: 'Origin Case ID',
        };
        return displayNames[fieldName] || fieldName;
    }
}
