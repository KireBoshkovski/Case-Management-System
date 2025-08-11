export interface Fork {
    id: number;
    title: string;
    description: string;
    alternativeDiagnosis: string;
    alternativeTreatment: string;
    analysisNotes: string;
    recommendations: string;
    originId: number;
    editorId: number;
}
