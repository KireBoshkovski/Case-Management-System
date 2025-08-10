import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicCaseDetails } from './public-case-details';

describe('PublicCaseDetails', () => {
    let component: PublicCaseDetails;
    let fixture: ComponentFixture<PublicCaseDetails>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [PublicCaseDetails],
        }).compileComponents();

        fixture = TestBed.createComponent(PublicCaseDetails);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
