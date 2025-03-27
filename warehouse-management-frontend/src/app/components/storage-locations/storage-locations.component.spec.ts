import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageLocationsComponent } from './storage-locations.component';

describe('StorageLocationsComponent', () => {
  let component: StorageLocationsComponent;
  let fixture: ComponentFixture<StorageLocationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StorageLocationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StorageLocationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
