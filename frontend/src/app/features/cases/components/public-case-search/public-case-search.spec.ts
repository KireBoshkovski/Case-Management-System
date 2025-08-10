import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicCaseSearch } from './public-case-search';

describe('PublicCaseSearch', () => {
    let component: PublicCaseSearch;
    let fixture: ComponentFixture<PublicCaseSearch>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [PublicCaseSearch],
        }).compileComponents();

        fixture = TestBed.createComponent(PublicCaseSearch);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
