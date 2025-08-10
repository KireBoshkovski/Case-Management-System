import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseSearch } from './case-search';

describe('CaseSearch', () => {
    let component: CaseSearch;
    let fixture: ComponentFixture<CaseSearch>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [CaseSearch],
        }).compileComponents();

        fixture = TestBed.createComponent(CaseSearch);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
