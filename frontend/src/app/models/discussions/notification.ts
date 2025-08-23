export interface Notification {
    id: number;
    discussionId: number;
    commentId?: number;
    message: string;
    createdAt: Date;
    read: boolean;
}
