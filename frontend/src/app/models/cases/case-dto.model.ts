export interface CaseDto {
    id: number;
    bloodType?: string;
    allergies?: string;
    description?: string;
    treatmentPlan?: string;
    status: String;
    createdAt?: string; // ISO string from backend
    updatedAt?: string; // ISO string from backend
    patientId: number;
    doctorId: number;
    examinationsIds: number[];
}
