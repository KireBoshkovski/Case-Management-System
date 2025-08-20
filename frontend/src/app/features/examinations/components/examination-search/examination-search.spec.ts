import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ExaminationSearch} from './examination-search';

describe('ExaminationSearch', () => {
    let component: ExaminationSearch;
    let fixture: ComponentFixture<ExaminationSearch>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ExaminationSearch],
        }).compileComponents();

        fixture = TestBed.createComponent(ExaminationSearch);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
