import { Routes } from '@angular/router';
import { CaseSearch } from './features/cases/components/case-search/case-search';
import { CaseDetail } from './features/cases/components/case-detail/case-detail';

import { MainLayout } from './layouts/main-layout/main-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';

import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { ForgotPassword } from './features/auth/forgot-password/forgot-password';
import { ResetPassword } from './features/auth/reset-password/reset-password';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        children: [
            { path: 'cases', component: CaseSearch },
            {
                path: 'cases/public',
                component: CaseSearch,
                data: { public: true },
            },
            { path: 'cases/:id', component: CaseDetail },
            {}
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
        ],
    },
];
