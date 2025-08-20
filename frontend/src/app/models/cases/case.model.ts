import {CaseStatus} from './case-status.enum';
import {Doctor} from '../doctor.model';
import {Examination} from '../examination.model';
import {Patient} from '../patient.model';
import {PublicCase} from './public-case.model';

export interface Case {
    id: number;
    bloodType?: string;
    allergies?: string;
    description?: string;
    treatmentPlan?: string;
    status: CaseStatus;
    createdAt: string;
    updatedAt: string;
    patient: Patient; // Full patient object
    doctor: Doctor; // Full doctor object
    examinations: Examination[];
    publishedCase?: PublicCase;
}
