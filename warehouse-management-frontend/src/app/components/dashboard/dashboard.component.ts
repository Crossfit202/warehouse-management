import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../services/alert.service';
import { Alert } from '../../models/Alert';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { InventoryService } from '../../services/inventory.service';
import { InventoryMovement } from '../../models/InventoryMovement';
import { MovementService } from '../../services/movement.service';
import { StorageLocationsService } from '../../services/storage-locations.service';
import { StorageLocationCapacity } from '../../models/StorageLocationCapacity';
import { WarehouseInventory } from '../../models/WarehouseInventory';
import { WarehousePersonnelService } from '../../services/personnel.service';
import { WarehousePersonnelDTO } from '../../services/personnel.service';
import { RouterModule } from '@angular/router'; // <-- Add this import
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], // <-- Add RouterModule here
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  alerts: Alert[] = [];
  warehouses: Warehouse[] = [];
  selectedWarehouse: string = '';
  selectedWarehouseId: string = '';
  inventoryItems: WarehouseInventory[] = [];
  recentMovements: InventoryMovement[] = [];
  storageLocations: StorageLocationCapacity[] = [];
  totalQuantity: number = 0;
  openAlertCount: number = 0;
  recentMovementCount: number = 0;
  warehouseCapacity: number = 0;
  warehouseLocation: string = '';
  personnel: WarehousePersonnelDTO[] = [];
  alertStatusFilter: string = '';
  movementStartDate: string = '';
  movementEndDate: string = '';
  filteredMovements: InventoryMovement[] = [];


  constructor(
    private alertService: AlertService,
    private warehouseService: WarehouseService,
    private inventoryService: InventoryService,
    private movementService: MovementService,
    private storageLocationService: StorageLocationsService,
    private warehousePersonnelService: WarehousePersonnelService
  ) { }

  ngOnInit(): void {
    this.loadWarehouses();
  }

  loadAlerts(): void {
    this.alertService.getAllAlerts().subscribe(data => {
      this.alerts = data;
      this.calculateOpenAlerts();
    });
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data;

      if (this.warehouses.length > 0) {
        const first = this.warehouses[0];
        this.selectedWarehouse = first.name;
        this.selectedWarehouseId = first.id;
        this.warehouseCapacity = first.max_capacity || 0;
        this.warehouseLocation = first.location || 'N/A';
        this.loadStorageLocations(this.selectedWarehouseId);
        this.loadAlerts();
        this.loadInventory(this.selectedWarehouseId);
        this.loadRecentMovements(this.selectedWarehouseId);
        this.loadPersonnel(this.selectedWarehouseId);
      }
    });
  }

  onSelect(event: Event): void {
    const selectedName = (event.target as HTMLInputElement).value;
    this.selectedWarehouse = selectedName;

    const selected = this.warehouses.find(w => w.name === selectedName);
    if (selected) {
      this.selectedWarehouseId = selected.id;
      this.warehouseCapacity = selected.max_capacity || 0;
      this.warehouseLocation = selected.location || 'N/A';
      this.loadInventory(this.selectedWarehouseId);
      this.loadAlerts();
      this.loadRecentMovements(this.selectedWarehouseId);
      this.loadStorageLocations(this.selectedWarehouseId);
      this.loadPersonnel(this.selectedWarehouseId);
    }
  }

  loadInventory(warehouseId: string): void {
    this.inventoryService.getInventoryForWarehouse(warehouseId).subscribe(data => {
      this.inventoryItems = data; // ✅ WarehouseInventory[] is directly assigned
      this.totalQuantity = data.reduce((sum, item) => sum + (item.quantity || 0), 0);
    });
  }


  calculateOpenAlerts(): void {
    this.openAlertCount = this.alerts.filter(
      alert =>
        alert.warehouse?.name === this.selectedWarehouse &&
        alert.status !== 'CLOSED'
    ).length;
  }


  loadRecentMovements(warehouseId: string): void {
    this.movementService.getMovementsForWarehouse(warehouseId).subscribe(data => {
      this.recentMovements = data;
      this.filteredMovements = data; // Show all by default
      this.recentMovementCount = data.length;
    });
  }

  loadStorageLocations(warehouseId: string): void {
    this.storageLocationService.getStorageLocationCapacities(warehouseId).subscribe(data => {
      this.storageLocations = data; // Update type to `StorageLocationCapacity[]`
    });
  }

  loadPersonnel(warehouseId: string): void {
    this.warehousePersonnelService.getPersonnelForWarehouse(warehouseId).subscribe(data => {
      this.personnel = data;
    });
  }

  onAddInventory() {
    // TODO: Open modal or add logic here
    console.log('Add Inventory clicked');
  }

  addStorageLocation() {
    // TODO: Open modal or add logic here
    console.log('Add Storage Location clicked');
  }

  onAddUser() {
    // TODO: Open modal or add logic here
    console.log('Add User clicked');
  }

  get filteredAlerts() {
    if (!this.alertStatusFilter) {
      return this.alerts.filter(alert => alert.warehouse?.name === this.selectedWarehouse);
    }
    return this.alerts.filter(
      alert =>
        alert.warehouse?.name === this.selectedWarehouse &&
        alert.status === this.alertStatusFilter
    );
  }

  filterMovements(): void {
    if (!this.movementStartDate && !this.movementEndDate) {
      this.filteredMovements = this.recentMovements;
      return;
    }
    this.filteredMovements = this.recentMovements.filter(movement => {
      const movementDateStr = new Date(movement.time).toISOString().slice(0, 10);
      if (this.movementStartDate && this.movementEndDate) {
        return (
          movementDateStr >= this.movementStartDate &&
          movementDateStr <= this.movementEndDate
        );
      } else if (this.movementStartDate) {
        return movementDateStr >= this.movementStartDate;
      } else if (this.movementEndDate) {
        return movementDateStr <= this.movementEndDate;
      }
      return true;
    });
  }

  clearMovementFilter(): void {
    this.movementStartDate = '';
    this.movementEndDate = '';
    this.filteredMovements = this.recentMovements;
  }
}
