import { CaseStatus } from './case-status.enum';
import { Examination } from './examination.model';

export interface Case {
    id?: number;
    bloodType?: string;
    allergies?: string;
    description?: string;
    treatmentPlan?: string;
    status: CaseStatus;
    createdAt?: string;
    updatedAt?: string;
    patientId: number;
    doctorId: number;
    examinations: Examination[];
}
