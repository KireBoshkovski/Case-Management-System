import {Component, EventEmitter, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
    selector: 'search-bar',
    imports: [CommonModule],
    templateUrl: './search-bar.html',
    styleUrl: './search-bar.css',
})
export class SearchBar {
    @Output() searchChange = new EventEmitter<string>();

    private changes$ = new Subject<string>();
    hasValue = false;

    constructor() {
        this.changes$
            .pipe(debounceTime(300), distinctUntilChanged())
            .subscribe(value => this.searchChange.emit(value));
    }

    onInput(event: Event) {
        const value = (event.target as HTMLInputElement).value || '';
        this.hasValue = value.length > 0;
        this.changes$.next(value);
    }

    clear() {
        this.hasValue = false;
        this.searchChange.emit('');
    }
}
