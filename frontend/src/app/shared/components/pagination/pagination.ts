import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'pagination',
    imports: [],
    templateUrl: './pagination.html',
    styleUrl: './pagination.css',
})
export class Pagination {
    @Input() currentPage: number = 1;
    @Input() totalElements: number = 0;
    @Input() resultsPerPage: number = 10;
    @Output() pageChange = new EventEmitter<number>();

    get totalPages(): number {
        return Math.ceil(this.totalElements / this.resultsPerPage);
    }

    get pages(): number[] {
        const pages = [];
        let startPage = Math.max(1, this.currentPage - 2);
        let endPage = Math.min(this.totalPages, this.currentPage + 2);

        if (this.currentPage <= 3) {
            endPage = Math.min(5, this.totalPages);
        }

        if (this.currentPage >= this.totalPages - 2) {
            startPage = Math.max(this.totalPages - 4, 1);
        }

        for (let i = startPage; i <= endPage; i++) {
            pages.push(i);
        }

        return pages;
    }

    changePage(page: number): void {
        if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
            this.currentPage = page;
            this.pageChange.emit(page);
        }
    }

    goToFirstPage(): void {
        this.changePage(1);
    }

    goToLastPage(): void {
        this.changePage(this.totalPages);
    }

    goToPreviousPage(): void {
        this.changePage(this.currentPage - 1);
    }

    goToNextPage(): void {
        this.changePage(this.currentPage + 1);
    }
}
