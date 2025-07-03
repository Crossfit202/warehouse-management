import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { FormsModule } from '@angular/forms';
import { StorageLocationsService } from '../../services/storage-locations.service';
import { StorageLocation } from '../../models/StorageLocation';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-warehouses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './warehouses.component.html',
  styleUrl: './warehouses.component.css'
})
export class WarehousesComponent implements OnInit {

  warehouses: Warehouse[] = [];
  showAddModal = false;
  showEditModal = false;
  showDeleteModal = false;
  showRemoveLocationModal = false;

  newWarehouse: Partial<Warehouse> = {};
  editWarehouse: Warehouse | null = null;
  deleteWarehouse: Warehouse | null = null;
  locationToRemove: any = null;

  expandedWarehouseId: string | null = null;
  storageLocations: { [warehouseId: string]: any[] } = {};

  newLocationName: string = '';
  newLocationMax: number | null = null;

  allStorageLocations: StorageLocation[] = [];
  selectedLocationId: string = '';
  newStorageLocationName: string = '';

  currentUser: any;
  isAdmin = false;
  isManager = false;
  isInvClerk = false;
  activeWarehouseIds: string[] = [];

  constructor(
    private warehouseService: WarehouseService,
    private storageLocationsService: StorageLocationsService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.isAdmin = this.currentUser?.role === 'ROLE_ADMIN';
    this.isManager = this.currentUser?.role === 'ROLE_MANAGER';
    this.isInvClerk = this.currentUser?.role === 'ROLE_INV_CLERK';

    this.loadWarehouses();
    this.storageLocationsService.getAllStorageLocations().subscribe(data => {
      this.allStorageLocations = data;
    });
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data.map(w => new Warehouse(w));
      // Find warehouses where manager is ACTIVE
      if (this.isManager) {
        this.activeWarehouseIds = this.warehouses
          .filter(w => w.personnel?.some((p: any) =>
            (p.userId === this.currentUser?.id || p.id === this.currentUser?.id) && p.status === 'ACTIVE'
          ))
          .map(w => w.id);
      }
      // Load storage locations for each warehouse
      this.warehouses.forEach(warehouse => {
        this.warehouseService.getStorageLocationsForWarehouse(warehouse.id).subscribe(locations => {
          this.storageLocations[warehouse.id] = locations;
        });
      });
    });
  }

  // Add Modal
  openAddModal() {
    if (!this.isAdmin) return;
    this.newWarehouse = {};
    this.showAddModal = true;
  }
  closeAddModal() {
    this.showAddModal = false;
  }
  addWarehouse() {
    if (!this.isAdmin) return;
    if (!this.newWarehouse.name || !this.newWarehouse.location || !this.newWarehouse.max_capacity) return;
    this.warehouseService.createWarehouse(this.newWarehouse).subscribe(() => {
      this.loadWarehouses();
      this.closeAddModal();
    });
  }

  // Edit Modal
  openEditModal(warehouse: Warehouse) {
    if (!this.canEditOrDelete(warehouse)) return;
    console.log('Edit clicked:', warehouse);
    this.editWarehouse = new Warehouse({
      id: warehouse.id,
      name: warehouse.name,
      location: warehouse.location,
      max_capacity: warehouse.max_capacity,
    });
    this.showEditModal = true;
  }
  closeEditModal() {
    this.showEditModal = false;
  }
  updateWarehouse() {
    if (!this.editWarehouse || !this.canEditOrDelete(this.editWarehouse)) return;
    this.warehouseService.updateWarehouse(this.editWarehouse.id, this.editWarehouse).subscribe(() => {
      this.loadWarehouses();
      this.closeEditModal();
    });
  }

  // Delete Modal
  openDeleteModal(warehouse: Warehouse) {
    if (!this.isAdmin) return;
    this.deleteWarehouse = warehouse;
    this.showDeleteModal = true;
  }
  closeDeleteModal() {
    this.showDeleteModal = false;
  }
  deleteWarehouseConfirmed() {
    if (!this.isAdmin || !this.deleteWarehouse) return;
    this.warehouseService.deleteWarehouse(this.deleteWarehouse.id).subscribe(() => {
      this.loadWarehouses();
      this.closeDeleteModal();
    });
  }

  addStorageLocation() {
    if (!this.editWarehouse || !this.selectedLocationId || !this.newStorageLocationName) return;
    const selectedLoc = this.allStorageLocations.find(loc => loc.id === this.selectedLocationId);
    if (!selectedLoc) return;
    this.warehouseService.createWarehouseStorageLocation({
      warehouseId: this.editWarehouse.id,
      storageLocationTemplateId: selectedLoc.id,
      name: this.newStorageLocationName,
      maxCapacity: selectedLoc.max_capacity
    }).subscribe(loc => {
      if (!this.storageLocations[this.editWarehouse!.id]) this.storageLocations[this.editWarehouse!.id] = [];
      this.storageLocations[this.editWarehouse!.id].push(loc);
      this.selectedLocationId = '';
      this.newStorageLocationName = '';
    });
  }

  openRemoveLocationModal(loc: any) {
    this.locationToRemove = loc;
    this.showRemoveLocationModal = true;
  }

  closeRemoveLocationModal() {
    this.showRemoveLocationModal = false;
    this.locationToRemove = null;
  }

  confirmRemoveLocation() {
    if (!this.editWarehouse || !this.locationToRemove) return;
    this.warehouseService.removeStorageLocationFromWarehouse(this.editWarehouse.id, this.locationToRemove.id).subscribe(() => {
      if (this.editWarehouse?.id && this.storageLocations[this.editWarehouse.id]) {
        this.storageLocations[this.editWarehouse.id] = this.storageLocations[this.editWarehouse.id].filter(l => l.id !== this.locationToRemove.id);
      }
      this.closeRemoveLocationModal();
    });
  }

  isLocationAssignedToEditWarehouse(locationId: string): boolean {
    if (!this.editWarehouse) return false;
    const assigned = this.storageLocations[this.editWarehouse.id] || [];
    return assigned.some(sl => sl.id === locationId);
  }

  // Helper for template
  canEditOrDelete(warehouse: Warehouse): boolean {
    if (this.isAdmin) return true;
    if (this.isManager) return this.activeWarehouseIds.includes(warehouse.id);
    return false;
  }

  canEditWarehouse(warehouse: Warehouse): boolean {
    if (this.isAdmin) return true;
    if (this.isManager) return this.activeWarehouseIds.includes(warehouse.id);
    return false;
  }
}
