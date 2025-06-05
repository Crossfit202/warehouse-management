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
  totalQuantity: number = 0;
  openAlertCount: number = 0;
  activeWarehouseCount: number = 0;
  recentMovementCount: number = 0;


  constructor(
    private alertService: AlertService,
    private warehouseService: WarehouseService,
    private inventoryService: InventoryService,
    private movementService: MovementService
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
      this.activeWarehouseCount = data.length;

      if (this.warehouses.length > 0) {
        this.selectedWarehouse = this.warehouses[0].name;
        this.selectedWarehouseId = this.warehouses[0].id;
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
      this.loadInventory(this.selectedWarehouseId);
      this.calculateOpenAlerts();
      this.loadRecentMovements(this.selectedWarehouseId);
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

}
