import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogFilters } from './catalog-filters';

describe('CatalogFilters', () => {
  let component: CatalogFilters;
  let fixture: ComponentFixture<CatalogFilters>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogFilters],
    }).compileComponents();

    fixture = TestBed.createComponent(CatalogFilters);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
