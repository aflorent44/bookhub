import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Fillin } from './fillin';

describe('Fillin', () => {
  let component: Fillin;
  let fixture: ComponentFixture<Fillin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Fillin],
    }).compileComponents();

    fixture = TestBed.createComponent(Fillin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
