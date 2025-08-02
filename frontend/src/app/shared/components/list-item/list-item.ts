import { Component, Input } from '@angular/core';
import { ColumnDef } from '../../../models/columnDef';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'list-item',
    imports: [RouterLink],
    templateUrl: './list-item.html',
    styleUrl: './list-item.css',
})
export class ListItem<T = unknown> {
    @Input() item!: T;
    @Input() columns!: ColumnDef<T>[];

    @Input() routePrefix: string = '';

    get idField(): any {
        const idColumn = this.columns.find((col) => col.idField);
        const fieldName = idColumn?.idField || 'id';
        return this.item[fieldName as keyof T];
    }
}
