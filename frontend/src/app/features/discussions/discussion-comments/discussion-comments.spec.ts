import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscussionComments } from './discussion-comments';

describe('DiscussionComments', () => {
  let component: DiscussionComments;
  let fixture: ComponentFixture<DiscussionComments>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiscussionComments]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiscussionComments);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
