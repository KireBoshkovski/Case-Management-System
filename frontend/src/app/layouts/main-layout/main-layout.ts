import { Component, inject, OnInit } from '@angular/core';
import { Navbar } from '../../shared/components/navbar/navbar';
import { ActivatedRoute, RouterOutlet } from '@angular/router';

@Component({
    selector: 'main-layout',
    imports: [Navbar, RouterOutlet],
    templateUrl: './main-layout.html',
    styleUrl: './main-layout.css',
})
export class MainLayout implements OnInit {
    currentRoute: string = '';
    route = inject(ActivatedRoute);

    ngOnInit() {
        
    }
}
