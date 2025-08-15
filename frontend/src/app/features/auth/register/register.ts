import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'register',
    imports: [ReactiveFormsModule, CommonModule, RouterLink],
    templateUrl: './register.html',
    styleUrl: './register.css',
})
export class Register {
    fb = inject(FormBuilder);
    authService = inject(AuthService);
    router = inject(Router);

    selectedRole: 'PATIENT' | 'DOCTOR' = 'PATIENT';
    errorMessage: string | null = null;
    successMessage: string | null = null;

    registerForm: FormGroup = this.fb.group({
        email: [
            '',
            [Validators.required, Validators.email, Validators.maxLength(50)],
        ],
        password: [
            '',
            [
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(40),
            ],
        ],
        firstName: ['', [Validators.required, Validators.maxLength(20)]],
        lastName: ['', [Validators.required, Validators.maxLength(20)]],

        role: ['PATIENT', Validators.required],

        // Patient fields
        dateOfBirth: [null],
        phoneNumber: [''],
        address: [''],
        gender: [''],

        // Doctor fields
        specialization: [''],
        department: [''],
    });

    get formControls() {
        return this.registerForm.controls;
    }

    toggleRole() {
        const currentRole = this.formControls['role'].value;
        const newRole = currentRole === 'PATIENT' ? 'DOCTOR' : 'PATIENT';

        this.formControls['role'].setValue(newRole);

        if (newRole === 'PATIENT') {
            this.formControls['specialization'].reset();
            this.formControls['department'].reset();
        } else {
            this.formControls['dateOfBirth'].reset();
            this.formControls['phoneNumber'].reset();
            this.formControls['address'].reset();
            this.formControls['gender'].reset();
        }
    }

    selectRole(role: 'PATIENT' | 'DOCTOR'): void {
        this.selectedRole = role;

        this.registerForm.get('role')?.setValue(role);

        if (role === 'PATIENT') {
            this.registerForm.get('specialization')?.reset();
            this.registerForm.get('department')?.reset();
        } else {
            this.registerForm.get('dateOfBirth')?.reset();
            this.registerForm.get('phoneNumber')?.reset();
            this.registerForm.get('address')?.reset();
            this.registerForm.get('gender')?.reset();
        }
    }

    onSubmit() {
        if (this.registerForm.invalid) {
            this.errorMessage = 'Please fill in all required fields correctly.';
            this.registerForm.markAllAsTouched();
            return;
        }

        this.errorMessage = null;
        this.successMessage = null;

        this.authService.signUp(this.registerForm.value).subscribe({
            next: () => {
                setTimeout(() => {
                    this.router.navigate(['/login']);
                }, 3000);
            },
            error: (response) => {
                this.errorMessage = response.error.error;
            },
        });
    }
}
