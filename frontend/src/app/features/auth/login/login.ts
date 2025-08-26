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
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'login',
    imports: [ReactiveFormsModule, FontAwesomeModule, RouterLink],
    templateUrl: './login.html',
    styleUrl: './login.css',
})
export class Login {
    fb = inject(FormBuilder);
    authService = inject(AuthService);
    router = inject(Router);
    toastService = inject(ToastrService);

    showPassword = false;
    faEye = faEye;
    faEyeSlash = faEyeSlash;

    loginForm: FormGroup = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(5)]],
    });

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
        }
    }

    togglePasswordVisibility() {
        this.showPassword = !this.showPassword;
    }
}
