import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookTile } from './book-tile';

describe('BookTile', () => {
  let component: BookTile;
  let fixture: ComponentFixture<BookTile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookTile],
    }).compileComponents();

    fixture = TestBed.createComponent(BookTile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
