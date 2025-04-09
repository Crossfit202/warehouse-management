import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StorageLocation } from '../../models/StorageLocation';
import { StorageLocationsService } from '../../services/storage-locations.service';

@Component({
  selector: 'app-storage-locations',
  imports: [CommonModule],
  templateUrl: './storage-locations.component.html',
  styleUrl: './storage-locations.component.css'
})
export class StorageLocationsComponent implements OnInit {

  storagelocations: StorageLocation[] = [];

  constructor(private StorageLocationsService: StorageLocationsService) {}

  ngOnInit(): void {
      this.loadStorageLocations();
  }

  loadStorageLocations(): void {
    this.StorageLocationsService.getAllStorageLocations().subscribe(data => {
      this.storagelocations = data;
    });
  }
}
