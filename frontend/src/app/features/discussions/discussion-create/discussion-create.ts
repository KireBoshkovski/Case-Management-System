import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DiscussionService} from '../../../core/services/discussion.service';
import {DiscussionDto} from '../../../models/discussions/discussion-dto';
import {CommonModule} from '@angular/common';

@Component({
    selector: 'app-create-discussion',
    imports: [FormsModule, CommonModule],
    templateUrl: 'discussion-create.html'
})
export class CreateDiscussionComponent {
    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private discussionService = inject(DiscussionService);

    title = '';
    description = '';
    loading = false;
    caseId: number = 0;

    ngOnInit() {
        this.caseId = Number(this.route.snapshot.queryParams['caseId']);
    }

    onSubmit() {
        this.loading = true;

        const discussion: Partial<DiscussionDto> = {
            title: this.title,
            description: this.description,
            caseId: this.caseId
        };

        console.log("CASE ID: ", this.caseId)

        this.discussionService.addDiscussion(discussion as DiscussionDto).subscribe({
            next: () => {
                this.router.navigate(['/public', this.caseId]);
            },
            error: (err) => {
                console.error(err);
                this.loading = false;
            }
        });
    }

    cancel() {
        this.router.navigate(['/public', this.caseId]);
    }
}
