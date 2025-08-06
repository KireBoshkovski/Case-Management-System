export interface JwtResponse {
    accessToken: string;
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: 'DOCTOR' | 'PATIENT';
}
