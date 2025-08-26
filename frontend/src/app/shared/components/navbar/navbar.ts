import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NotificationBell } from '../notification-bell/notification-bell';

@Component({
    selector: 'navbar',
    imports: [RouterLink, NotificationBell],
    templateUrl: './navbar.html',
    styleUrl: './navbar.css',
})
export class Navbar {}
