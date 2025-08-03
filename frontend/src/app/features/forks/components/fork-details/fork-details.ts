import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ForkService } from '../../../../core/services/fork.service';
import { Fork } from '../../../../models/fork';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'fork-details',
    imports: [DatePipe],
    templateUrl: './fork-details.html',
    styleUrl: './fork-details.css',
})
export class ForkDetails implements OnInit {
    route = inject(ActivatedRoute);
    forkService = inject(ForkService);
    fork: Fork | undefined;

    ngOnInit(): void {
        const forkId = this.route.snapshot.paramMap.get('id');
        if (forkId) {
            this.forkService.getFork(+forkId).subscribe({
                next: (fork) => {
                    this.fork = fork;
                },
            });
        } else {
            console.error('No fork ID provided in route parameters.');
        }
    }
}
