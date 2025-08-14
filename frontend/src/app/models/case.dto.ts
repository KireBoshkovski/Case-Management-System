import { PatientDetailsModel } from './patient-details.model';
import {CaseStatus} from './cases/case-status.enum';

export interface CaseDto {
    id: number;
    public: boolean;
    bloodType?: string | null;
    allergies?: string | null;
    description?: string | null;
    treatmentPlan?: string | null;
    status: CaseStatus;
    createdAt: string;   // ISO
    updatedAt: string;   // ISO
    patient: PatientDetailsModel;
    doctorId: number;
    examinationIds: number[];
}
