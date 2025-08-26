import {Routes} from '@angular/router';
import {CaseSearch} from './features/cases/components/case-search/case-search';
import {CaseDetail} from './features/cases/components/case-detail/case-detail';

import {MainLayout} from './layouts/main-layout/main-layout';
import {AuthLayout} from './layouts/auth-layout/auth-layout';

import {CreateDiscussionComponent} from './features/discussions/discussion-create/discussion-create';
import {Login} from './features/auth/login/login';
import {Register} from './features/auth/register/register';
import {PatientSearch} from './features/patients/components/patient-search/patient-search';
import {PatientDetails} from './features/patients/components/patient-details/patient-details';
import {AuthGuard} from './core/guards/auth-guard';
import {Logout} from './features/auth/logout/logout';
import {PublicCaseSearch} from './features/cases/components/public-case-search/public-case-search';
import {PublicCaseDetails} from './features/cases/components/public-case-details/public-case-details';
import {CreateCase} from './features/cases/components/create-case/create-case';
import {PublishCase} from './features/cases/components/publish-case/publish-case';
import {ExaminationDetails} from './features/examinations/components/examination-details/examination-details';
import {DiscussionComments} from './features/discussions/discussion-comments/discussion-comments';
import {DiscussionList} from './features/discussions/discussion-list/discussion-list';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        canActivate: [AuthGuard],
        children: [
            // Cases
            {path: 'public', component: PublicCaseSearch},
            {path: 'public/:id', component: PublicCaseDetails},

            {path: 'cases', component: CaseSearch},
            {path: 'cases/new', component: CreateCase},
            {path: 'cases/:id', component: CaseDetail},
            {path: 'cases/:id/edit', component: CreateCase},
            {path: 'cases/:id/publish', component: PublishCase},

            // Patients
            {path: 'patients', component: PatientSearch},
            {path: 'patients/:id', component: PatientDetails},

            // Threads (Discussions)
            {path: 'threads/create', component: CreateDiscussionComponent},
            {path: 'threads/:id', component: DiscussionComments},
            {path: 'threads', component: DiscussionList},

            // Examinations
            {path: 'examinations/:id', component: ExaminationDetails},
            {
                path: 'public',
                children: [
                    {path: '', component: PublicCaseSearch},
                    {path: ':id', component: PublicCaseDetails},
                ],
            },
            {
                path: 'cases',
                children: [
                    {path: '', component: CaseSearch},
                    {path: 'new', component: CreateCase},
                    {path: ':id', component: CaseDetail},
                    {path: ':id/edit', component: CreateCase},
                    {path: ':id/publish', component: PublishCase},
                ],
            },
            {
                path: 'patients',
                children: [
                    {path: '', component: PatientSearch},
                    {path: ':id', component: PatientDetails},
                ],
            },
            {
                path: 'discussions',
                children: [
                    {path: '', component: DiscussionList},
                    {path: ':id', component: DiscussionComments},
                ],
            },
            {
                path: 'examinations',
                children: [{path: ':id', component: ExaminationDetails}],
            },
        ],
    },
    {
        path: '',
        component: AuthLayout,
        children: [
            {path: 'login', component: Login},
            {path: 'register', component: Register},
            {path: 'logout', component: Logout},
        ],
    },
];
