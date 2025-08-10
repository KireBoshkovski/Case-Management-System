export interface Patient {
    id: number;
    firstName: string;
    lastName: string;
    email?: string;
    gender: string;
    dateOfBirth: string; // ISO date string
}
