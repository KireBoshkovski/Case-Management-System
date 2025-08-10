import { PublicCase } from './public-case.model';

export interface PublishCaseRequest {
    publicCase: PublicCase;
    originalCaseId: number;
}
