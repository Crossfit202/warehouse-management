import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { FormsModule } from '@angular/forms';

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

  newWarehouse: Partial<Warehouse> = {};
  editWarehouse: Warehouse | null = null;
  deleteWarehouse: Warehouse | null = null;

  expandedWarehouseId: string | null = null;
  storageLocations: { [warehouseId: string]: any[] } = {};

  constructor(private warehouseService: WarehouseService) { }

  ngOnInit(): void {
    this.loadWarehouses();
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data.map(w => new Warehouse(w));
    });
  }

  // Add Modal
  openAddModal() {
    this.newWarehouse = {};
    this.showAddModal = true;
  }
  closeAddModal() {
    this.showAddModal = false;
  }
  addWarehouse() {
    if (!this.newWarehouse.name || !this.newWarehouse.location || !this.newWarehouse.max_capacity) return;
    this.warehouseService.createWarehouse(this.newWarehouse).subscribe(() => {
      this.loadWarehouses();
      this.closeAddModal();
    });
  }

  // Edit Modal
  openEditModal(warehouse: Warehouse) {
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
    if (!this.editWarehouse) return;
    this.warehouseService.updateWarehouse(this.editWarehouse.id, this.editWarehouse).subscribe(() => {
      this.loadWarehouses();
      this.closeEditModal();
    });
  }

  // Delete Modal
  openDeleteModal(warehouse: Warehouse) {
    console.log('Delete clicked:', warehouse);
    this.deleteWarehouse = warehouse;
    this.showDeleteModal = true;
  }
  closeDeleteModal() {
    this.showDeleteModal = false;
  }
  deleteWarehouseConfirmed() {
    if (!this.deleteWarehouse) return;
    this.warehouseService.deleteWarehouse(this.deleteWarehouse.id).subscribe(() => {
      this.loadWarehouses();
      this.closeDeleteModal();
    });
  }

  toggleExpand(warehouse: Warehouse) {
    if (this.expandedWarehouseId === warehouse.id) {
      this.expandedWarehouseId = null;
      return;
    }
    this.expandedWarehouseId = warehouse.id;
    if (!this.storageLocations[warehouse.id]) {
      this.warehouseService.getStorageLocationsForWarehouse(warehouse.id).subscribe(locations => {
        this.storageLocations[warehouse.id] = locations;
      });
    }
  }
}
