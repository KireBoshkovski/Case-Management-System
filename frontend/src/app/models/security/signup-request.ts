export interface SignupRequest {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    role: 'DOCTOR' | 'PATIENT';

    // Optional fields for Patient
    dateOfBirth?: string;
    phoneNumber?: string;
    address?: string;
    gender?: 'MALE' | 'FEMALE';

    // Optional fields for Doctor
    specialization?: string;
    department?: string;
}
