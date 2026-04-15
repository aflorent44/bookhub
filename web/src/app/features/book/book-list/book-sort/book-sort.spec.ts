import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookSort } from './book-sort';

describe('BookSort', () => {
  let component: BookSort;
  let fixture: ComponentFixture<BookSort>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookSort],
    }).compileComponents();

    fixture = TestBed.createComponent(BookSort);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
