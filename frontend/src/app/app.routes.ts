import { Routes } from '@angular/router';
import { CaseSearch } from './features/cases/components/case-search/case-search';
import { CaseDetail } from './features/cases/components/case-detail/case-detail';

import { MainLayout } from './layouts/main-layout/main-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';

import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { PatientSearch } from './features/patients/components/patient-search/patient-search';
import { PatientDetails } from './features/patients/components/patient-details/patient-details';
import { AuthGuard } from './core/guards/auth-guard';
import { Logout } from './features/auth/logout/logout';
import { PublicCaseSearch } from './features/cases/components/public-case-search/public-case-search';
import { PublicCaseDetails } from './features/cases/components/public-case-details/public-case-details';
import { CreateCase } from './features/cases/components/create-case/create-case';
import { PublishCase } from './features/cases/components/publish-case/publish-case';
import { ForkSearch } from './features/forks/components/fork-search/fork-search';
import { ForkDetails } from './features/forks/components/fork-details/fork-details';
import { CreateFork } from './features/forks/components/create-fork/create-fork';
import { ExaminationDetails } from './features/examinations/components/examination-details/examination-details';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        canActivate: [AuthGuard],
        children: [
            // Cases
            { path: 'public', component: PublicCaseSearch },
            { path: 'public/:id', component: PublicCaseDetails },

            { path: 'cases', component: CaseSearch },
            { path: 'cases/:id', component: CaseDetail },
            { path: 'cases/new', component: CreateCase },
            { path: 'cases/:id/edit', component: CreateCase },

            { path: 'cases/:id/publish', component: PublishCase },

            // Patients
            { path: 'patients', component: PatientSearch },
            { path: 'patients/:id', component: PatientDetails },

            // Forks
            { path: 'forks', component: ForkSearch },
            { path: 'forks/:id', component: ForkDetails },
            { path: 'forks/new', component: CreateFork },
            { path: 'forks/:id/edit', component: CreateFork },

            // Examinations
            { path: 'examinations/:id', component: ExaminationDetails },
        ],
    },
    {
        path: '',
        component: AuthLayout,
        children: [
            { path: 'login', component: Login },
            { path: 'register', component: Register },
            { path: 'logout', component: Logout },
        ],
    },
];
