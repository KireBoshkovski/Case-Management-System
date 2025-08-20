import {Component, Input} from '@angular/core';
import {ListItem} from '../list-item/list-item';
import {ColumnDef} from '../../../models/columnDef';

@Component({
    selector: 'list',
    imports: [ListItem],
    standalone: true,
    templateUrl: './list.html',
    styleUrl: './list.css',
})
export class List<T = unknown> {
    @Input() items: T[] = [];
    @Input() columns!: ColumnDef<T>[];
    @Input() routePrefix: string = '';
}
