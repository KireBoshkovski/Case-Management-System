export interface PageResponse<T> {
    content: T[];
    page: number;            // 0-based
    size: number;
    totalElements: number;
    totalPages: number;
    hasNext: boolean;
    hasPrevious: boolean;
    last?: boolean;
}
