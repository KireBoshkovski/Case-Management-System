import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';

@Component({
    selector: 'navbar',
    standalone: true,
    imports: [
        RouterLink
    ],
    templateUrl: './navbar.html',
    styleUrls: ['./navbar.css']
})
export class Navbar {

}
