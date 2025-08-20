import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'search-bar',
    imports: [],
    templateUrl: './search-bar.html',
    styleUrl: './search-bar.css',
})
export class SearchBar {
    @Output() search = new EventEmitter<string>();
    @Input() placeholder: string = 'Search...';

    onInputChange(query: string) {
        console.log(query);
        this.search.emit(query);
    }
}
