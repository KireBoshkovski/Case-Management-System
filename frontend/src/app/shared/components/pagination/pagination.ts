import {Component, EventEmitter, Input, Output} from '@angular/core';

export interface PageInfo {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
}

@Component({
    selector: 'pagination',
    imports: [],
    templateUrl: './pagination.html',
    styleUrl: './pagination.css',
})
export class Pagination {
    @Input() page!: PageInfo;
    @Output() pageChange = new EventEmitter<number>();

    get currentPage(): number {
        return this.page.number + 1;
    }

    get pages(): number[] {
        const pages: number[] = [];
        let startPage = Math.max(1, this.currentPage - 2);
        let endPage = Math.min(this.page.totalPages, this.currentPage + 2);

        if (this.currentPage <= 3) {
            endPage = Math.min(5, this.page.totalPages);
        }
        if (this.currentPage >= this.page.totalPages - 2) {
            startPage = Math.max(this.page.totalPages - 4, 1);
        }

        for (let i = startPage; i <= endPage; i++) {
            pages.push(i);
        }
        return pages;
    }

    changePage(page: number): void {
        if (
            page >= 1 &&
            page <= this.page.totalPages &&
            page !== this.currentPage
        ) {
            this.pageChange.emit(page);
        }
    }

    goToFirstPage(): void {
        this.changePage(1);
    }

    goToLastPage(): void {
        this.changePage(this.page.totalPages);
    }

    goToPreviousPage(): void {
        this.changePage(this.currentPage - 1);
    }

    goToNextPage(): void {
        this.changePage(this.currentPage + 1);
    }
}
