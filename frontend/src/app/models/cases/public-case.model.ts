import { PublicExamination } from '../public-examination';

export interface PublicCase {
    id: number;
    bloodType?: string;
    allergies?: string;
    description?: string;
    treatmentPlan?: string;
    patientAgeRange?: string;
    patientGender?: string;
    createdAt: string;
    updatedAt: string;
    publishedAt: string;
    examinations: PublicExamination[];
    viewsCount: number;
}
