import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscussionList } from './discussion-list';

describe('DiscussionList', () => {
  let component: DiscussionList;
  let fixture: ComponentFixture<DiscussionList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiscussionList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiscussionList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
