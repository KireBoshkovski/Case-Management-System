export interface DiscussionDto {
    id: number;
    title: string;
    description: string;
    createdAt: string;
    userId: number;
    caseId: number;
    commentsCount: number;
}
