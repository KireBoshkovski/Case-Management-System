import { Component, inject, Input, OnInit } from '@angular/core';
import { ForkListItem } from '../../../../models/forks/fork-list-item.model';
import { ForkService } from '../../../../core/services/fork.service';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { SearchBar } from '../../../../shared/components/search-bar/search-bar';

@Component({
    selector: 'fork-search',
    imports: [List, Pagination, SearchBar],
    templateUrl: './fork-search.html',
    styleUrl: './fork-search.css',
})
export class ForkSearch implements OnInit {
    forkService = inject(ForkService);
    @Input() caseId: number | null = null;

    forks: ForkListItem[] = [];

    forkColumns: ColumnDef<ForkListItem>[] = [
        { field: 'id', header: 'ID' },
        { field: 'title', header: 'Title' },
        { field: 'originId', header: 'Origin ID' },
        { field: 'editorId', header: 'Editor ID' },
    ];

    ngOnInit(): void {
        this.forkService.getForksByCaseId(this.caseId!).subscribe({
            next: (forks) => {
                this.forks = forks;
            },
            error: (error) => {
                console.error('Error fetching forks:', error);
            },
        });
    }
}
