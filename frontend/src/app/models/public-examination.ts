export interface PublicExamination {
    id: number;
    originalExaminationId: number;
    examinationType: string;
    findings?: string;
    results?: string;
    notes?: string;
    vitalSigns?: string;
    examinationDate?: string;
    examiningDoctorSpecialty?: string;
    publishedAt: string;
}
