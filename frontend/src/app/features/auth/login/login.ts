import { Component, inject } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'login',
    imports: [ReactiveFormsModule],
    templateUrl: './login.html',
    styleUrl: './login.css',
})
export class Login {
    fb = inject(FormBuilder);
    authService = inject(AuthService);
    router = inject(Router);

    loginForm: FormGroup = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
    });
    errorMessage: string | undefined;

    onSubmit() {
        if (this.loginForm.valid) {
            this.authService.signIn(this.loginForm.value).subscribe({
                next: () => {
                    this.router.navigate(['/cases']);
                },
                error: (error) => {
                    this.errorMessage =
                        error.message || 'Login failed. Please try again.';
                },
            });
        } else {
            this.errorMessage = 'Please fill in all required fields correctly.';
            return;
        }
    }
}
