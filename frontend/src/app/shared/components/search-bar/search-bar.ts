import { Component, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'search-bar',
    imports: [],
    templateUrl: './search-bar.html',
    styleUrl: './search-bar.css',
})
export class SearchBar {
    @Output() search = new EventEmitter<string>();

    onInputChange(query: string) {
        console.log(query);
        this.search.emit(query);
    }
}
