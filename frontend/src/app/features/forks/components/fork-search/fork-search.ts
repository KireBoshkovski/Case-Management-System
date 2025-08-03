import { Component, inject, Input, OnInit } from '@angular/core';
import { ForkListItem } from '../../../../models/fork-list-item.model';
import { ForkService } from '../../../../core/services/fork.service';
import { ActivatedRoute } from '@angular/router';
import { List } from '../../../../shared/components/list/list';
import { ColumnDef } from '../../../../models/columnDef';

@Component({
    selector: 'fork-search',
    imports: [List],
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
