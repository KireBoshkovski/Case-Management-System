import { PublicCase } from './public-case';

export interface PublishCaseRequest {
    publicCase: PublicCase;
    originalCaseId: number;
}
