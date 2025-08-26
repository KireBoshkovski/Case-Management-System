import {Component, inject, OnInit} from '@angular/core';
import {CaseService} from '../../../../core/services/case.service';
import {DiscussionService} from '../../../../core/services/discussion.service';
import {PublicCase} from '../../../../models/cases/public-case.model';
import {DiscussionDto} from '../../../../models/discussions/discussion-dto';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {Observable} from 'rxjs';
import {AsyncPipe, SlicePipe} from '@angular/common';

@Component({
    selector: 'public-case-details',
    imports: [RouterLink, AsyncPipe, SlicePipe],
    templateUrl: './public-case-details.html',
    styleUrl: './public-case-details.css',
})
export class PublicCaseDetails implements OnInit {
    caseService = inject(CaseService);
    discussionService = inject(DiscussionService);
    route = inject(ActivatedRoute);
    router = inject(Router);

    caseData: PublicCase | undefined;
    discussions$: Observable<DiscussionDto[]> | undefined;

    ngOnInit() {
        const caseId = Number(this.route.snapshot.paramMap.get('id'));

        if (caseId) {
            // Load public case data
            this.caseService.getPublicCaseById(caseId).subscribe({
                next: (caseData) => {
                    this.caseData = caseData;
                },
                error: (err) => {
                    console.error('Error fetching public case:', err);
                },
            });

            // Load discussions for this case
            this.discussions$ =
                this.discussionService.getDiscussionsByCase(caseId);
        } else {
            console.error('Invalid case ID');
        }
    }

    formatDate(dateString: string): string {
        if (!dateString) return 'Not specified';

        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    }

    formatDiscussionDate(dateString: string): string {
        const date = new Date(dateString);
        const now = new Date();
        const diffTime = Math.abs(now.getTime() - date.getTime());
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        if (diffDays === 1) {
            return 'Today';
        } else if (diffDays === 2) {
            return 'Yesterday';
        } else if (diffDays <= 7) {
            return `${diffDays - 1} days ago`;
        } else {
            return date.toLocaleDateString('en-US', {
                month: 'short',
                day: 'numeric',
            });
        }
    }
}
