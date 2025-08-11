import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ForkService } from '../../../../core/services/fork.service';
import { Fork } from '../../../../models/forks/fork';

@Component({
    selector: 'fork-details',
    imports: [],
    templateUrl: './fork-details.html',
    styleUrl: './fork-details.css',
})
export class ForkDetails implements OnInit {
    route = inject(ActivatedRoute);
    forkService = inject(ForkService);

    fork: Fork | undefined;

    ngOnInit() {
        const forkId = Number(this.route.snapshot.params['id']);

        if (forkId) {
            this.forkService.getFork(forkId).subscribe({
                next: (fork) => {
                    console.log('Fork details:', fork);
                    this.fork = fork;
                },
                error: (error) => {
                    console.error('Error fetching fork details:', error);
                },
            });
        }
    }
}
