export interface JwtResponse {
    accessToken: string;
    refreshToken: string;
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: 'DOCTOR' | 'PATIENT';
}
