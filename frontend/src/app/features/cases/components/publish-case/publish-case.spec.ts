import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublishCase } from './publish-case';

describe('PublishCase', () => {
    let component: PublishCase;
    let fixture: ComponentFixture<PublishCase>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [PublishCase],
        }).compileComponents();

        fixture = TestBed.createComponent(PublishCase);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
