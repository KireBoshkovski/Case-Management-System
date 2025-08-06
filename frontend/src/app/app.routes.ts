import { Routes } from '@angular/router';
import { CaseSearch } from './features/cases/components/case-search/case-search';
import { CaseDetail } from './features/cases/components/case-detail/case-detail';

import { MainLayout } from './layouts/main-layout/main-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';

import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { ForgotPassword } from './features/auth/forgot-password/forgot-password';
import { ResetPassword } from './features/auth/reset-password/reset-password';
import { PatientSearch } from './features/patients/components/patient-search/patient-search';
import { PatientDetails } from './features/patients/components/patient-details/patient-details';
import { ForkDetails } from './features/forks/components/fork-details/fork-details';
import { authGuard } from './core/guards/auth-guard';
import { Logout } from './features/auth/logout/logout';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        canActivate: [authGuard],
        children: [
            // Cases
            { path: 'cases', component: CaseSearch },
            {
                path: 'cases/public',
                component: CaseSearch,
                data: { public: true },
            },
            { path: 'cases/:id', component: CaseDetail },
            // Patients
            { path: 'patients', component: PatientSearch },
            { path: 'patients/:id', component: PatientDetails },
            // Forks
            { path: 'forks/:id', component: ForkDetails },
        ],
    },
    {
        path: 'auth',
        component: AuthLayout,
        children: [
            { path: '', redirectTo: 'login', pathMatch: 'full' },
            { path: 'login', component: Login },
            { path: 'register', component: Register },
            { path: 'forgot-password', component: ForgotPassword },
            { path: 'reset-password', component: ResetPassword },
            { path: 'logout', component: Logout },
        ],
    },
];
