import { Doctor } from './doctor.model';

export interface Examination {
    id: number;
    caseId: number;
    examinationType: string;
    examinationDate: string; // ISO date string
    findings: string;
    results: string;
    notes: string;
    vitalSigns: string;
    doctor: Doctor;
}
