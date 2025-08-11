import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFork } from './create-fork';

describe('CreateFork', () => {
  let component: CreateFork;
  let fixture: ComponentFixture<CreateFork>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateFork]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateFork);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
