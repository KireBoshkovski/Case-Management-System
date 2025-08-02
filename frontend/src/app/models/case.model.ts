import { CaseStatus } from './case-status.enum';
import { Doctor } from './doctor.model';
import { Examination } from './examination.model';
import { PatientDetailsModel } from './patient-details.model';

export interface Case {
    id: number;
    public: boolean;
    bloodType?: string;
    allergies?: string;
    description?: string;
    treatmentPlan?: string;
    status: CaseStatus;
    createdAt: string; // ISO date string
    updatedAt: string; // ISO date string
    patient: PatientDetailsModel;
    doctor: Doctor;
    examinations: Examination[];
}
