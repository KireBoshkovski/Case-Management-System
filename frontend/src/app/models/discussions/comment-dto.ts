export interface CommentDto {
    id?: number;
    content: string;
    createdAt?: string;
    userId?: number;
    discussionId: number;
    parentId?: number;
    replies?: CommentDto[];
}
