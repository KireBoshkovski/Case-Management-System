import { Component, inject } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'login',
    imports: [ReactiveFormsModule, RouterLink],
    templateUrl: './login.html',
    styleUrl: './login.css',
})
export class Login {
    fb = inject(FormBuilder);
    authService = inject(AuthService);
    router = inject(Router);
    toastService = inject(ToastrService);

    loginForm: FormGroup = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(5)]],
    });
    errorMessage: string | undefined;

    onSubmit() {
        if (this.loginForm.valid) {
            this.authService.signIn(this.loginForm.value).subscribe({
                next: () => {
                    setTimeout(() => {
                        this.router.navigate(['/cases']);
                    }, 2000);
                },
                error: (response) => {
                    this.toastService.error(response.error.error);
                },
            });
        } else {
            this.errorMessage = 'Please fill in all required fields correctly.';
            return;
        }
    }
}
