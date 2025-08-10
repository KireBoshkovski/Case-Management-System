import { Case } from './cases/case.model';
import { Doctor } from './doctor.model';

export interface Fork {
    id: number;
    title: string;
    description: string;
    alternativeDiagnosis: string;
    alternativeTreatment: string;
    analysisNotes: string;
    recommendations: string;
    origin: Case;
    editor: Doctor;
}
