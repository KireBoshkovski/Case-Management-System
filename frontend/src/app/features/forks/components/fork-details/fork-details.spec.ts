import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForkDetails } from './fork-details';

describe('ForkDetails', () => {
  let component: ForkDetails;
  let fixture: ComponentFixture<ForkDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForkDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForkDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
