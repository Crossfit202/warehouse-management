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
  inventoryItems: WarehouseInventory[] = [];
  recentMovements: InventoryMovement[] = [];
  storageLocations: StorageLocationCapacity[] = [];
  totalQuantity: number = 0;
  openAlertCount: number = 0;
  recentMovementCount: number = 0;
  warehouseCapacity: number = 0;
  warehouseLocation: string = '';
  personnel: WarehousePersonnelDTO[] = [];


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
    this.storageLocationService.getStorageLocationCapacities(warehouseId).subscribe(data => {
      this.storageLocations = data; // Update type to `StorageLocationCapacity[]`
    });
  }

  loadPersonnel(warehouseId: string): void {
    this.warehousePersonnelService.getPersonnelForWarehouse(warehouseId).subscribe(data => {
      this.personnel = data;
    });
  }
}
