import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StorageLocation } from '../../models/StorageLocation';
import { StorageLocationsService } from '../../services/storage-locations.service';

@Component({
  selector: 'app-storage-locations',
  imports: [CommonModule, FormsModule],
  templateUrl: './storage-locations.component.html',
  styleUrl: './storage-locations.component.css'
})
export class StorageLocationsComponent implements OnInit {
  storagelocations: any[] = [];
  showAddModal = false;
  showEditModal = false;
  showDeleteModal = false;
  newLocation = { name: '', max_capacity: 0 };
  editLocation: any = null;
  deleteLocation: any = null;
  searchTerm: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(private StorageLocationsService: StorageLocationsService) { }

  ngOnInit(): void {
    this.loadStorageLocations();
  }

  loadStorageLocations(): void {
    this.StorageLocationsService.getAllStorageLocations().subscribe(data => {
      // Optionally, fetch reference info for each location
      this.storagelocations = data;
      this.storagelocations.forEach(loc => {
        this.StorageLocationsService.isLocationReferenced(loc.id).subscribe(ref => {
          loc.isReferenced = ref;
        });
      });
    });
  }

  openAddModal() {
    this.newLocation = { name: '', max_capacity: 0 };
    this.showAddModal = true;
  }
  closeAddModal() { this.showAddModal = false; }

  addStorageLocation() {
    this.StorageLocationsService.createStorageLocation(this.newLocation).subscribe(() => {
      this.closeAddModal();
      this.loadStorageLocations();
    });
  }

  openEditModal(location: any) {
    this.editLocation = { ...location };
    this.showEditModal = true;
  }
  closeEditModal() { this.showEditModal = false; }

  updateStorageLocation() {
    this.StorageLocationsService.updateStorageLocation(this.editLocation.id, this.editLocation).subscribe(() => {
      this.closeEditModal();
      this.loadStorageLocations();
    });
  }

  openDeleteModal(location: any) {
    this.deleteLocation = location;
    this.showDeleteModal = true;
  }
  closeDeleteModal() { this.showDeleteModal = false; }

  deleteStorageLocation() {
    this.StorageLocationsService.deleteStorageLocation(this.deleteLocation.id).subscribe({
      next: () => {
        this.closeDeleteModal();
        this.loadStorageLocations();
      },
      error: (err) => {
        if (err.status === 409) {
          alert('Cannot delete: This storage location is in use by a warehouse or inventory.');
        } else {
          alert('Failed to delete storage location.');
        }
      }
    });
  }

  get filteredLocations(): any[] {
    if (!this.searchTerm.trim()) return this.storagelocations;
    const term = this.searchTerm.trim().toLowerCase();
    return this.storagelocations.filter(loc =>
      (loc.name?.toLowerCase().includes(term) ||
       loc.max_capacity?.toString().toLowerCase().includes(term))
    );
  }

  setSortByCapacity() {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
  }

  get sortedLocations(): any[] {
    const arr = [...this.filteredLocations];
    arr.sort((a, b) => {
      const cmp = a.max_capacity - b.max_capacity;
      return this.sortDirection === 'asc' ? cmp : -cmp;
    });
    return arr;
  }
}
