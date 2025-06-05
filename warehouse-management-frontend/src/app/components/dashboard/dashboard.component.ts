import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../services/alert.service';
import { Alert } from '../../models/Alert';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { InventoryService } from '../../services/inventory.service';
import { InventoryItem } from '../../models/InventoryItem';
import { InventoryMovement } from '../../models/InventoryMovement';
import { MovementService } from '../../services/movement.service';
import { StorageLocation } from '../../models/StorageLocation';
import { StorageLocationsService } from '../../services/storage-locations.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  alerts: Alert[] = [];
  warehouses: Warehouse[] = [];
  selectedWarehouse: string = '';
  selectedWarehouseId: string = '';
  inventoryItems: InventoryItem[] = [];
  recentMovements: InventoryMovement[] = [];
  storageLocations: StorageLocation[] = [];
  totalQuantity: number = 0;
  openAlertCount: number = 0;
  recentMovementCount: number = 0;
  warehouseCapacity: number = 0;
  warehouseLocation: string = '';



  constructor(
    private alertService: AlertService,
    private warehouseService: WarehouseService,
    private inventoryService: InventoryService,
    private movementService: MovementService,
    private storageLocationService: StorageLocationsService
  ) { }

  ngOnInit(): void {
    this.loadAlerts();
    this.loadWarehouses();
  }

  loadAlerts(): void {
    this.alertService.getAllAlerts().subscribe(data => {
      this.alerts = data;
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
        this.loadInventory(this.selectedWarehouseId);
        this.calculateOpenAlerts();
        this.loadRecentMovements(this.selectedWarehouseId);
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
      this.calculateOpenAlerts();
      this.loadRecentMovements(this.selectedWarehouseId);
      this.loadStorageLocations(this.selectedWarehouseId);
    }
  }

  loadInventory(warehouseId: string): void {
    this.inventoryService.getInventoryForWarehouse(warehouseId).subscribe(data => {
      this.inventoryItems = data;
      this.totalQuantity = data.reduce((sum, item) => sum + (item.quantity || 0), 0);
    });
  }

  calculateOpenAlerts(): void {
    this.openAlertCount = this.alerts.filter(
      alert => alert.warehouse?.name === this.selectedWarehouse
    ).length;
  }


  loadRecentMovements(warehouseId: string): void {
    this.movementService.getMovementsForWarehouse(warehouseId).subscribe(data => {
      this.recentMovements = data.slice(0, 5);
      this.recentMovementCount = data.length;
    });
  }

  loadStorageLocations(warehouseId: string): void {
    this.storageLocationService.getStorageLocationsByWarehouse(warehouseId).subscribe(data => {
      this.storageLocations = data;
    });
  }
}
