import { Component, OnInit } from '@angular/core';
import { InventoryService } from '../../services/inventory.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WarehouseService } from '../../services/warehouse.service';
import { StorageLocation } from '../../models/StorageLocation';
import { WarehouseInventory } from '../../models/WarehouseInventory';
import { MoveInventoryDTO } from '../../models/MoveInventoryDTO';
import { AuthService } from '../../services/auth.service'; // ✅ To get logged-in user
import { ToastrService } from 'ngx-toastr';
import { AlertService } from '../../services/alert.service'; // Import your alert service

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent implements OnInit {
  inventory: WarehouseInventory[] = [];
  warehouses: any[] = [];
  selectedWarehouse: string = '';

  // Modal State
  showMoveModal: boolean = false;
  selectedItem: WarehouseInventory | null = null;
  moveToWarehouseId: string = '';
  moveToLocationId: string = '';
  moveQuantity: number = 1;
  availableLocations: StorageLocation[] = [];
  availableToLocations: StorageLocation[] = [];

  userId: string = ''; // ✅ For logging movement

  constructor(
    private inventoryService: InventoryService,
    private warehouseService: WarehouseService,
    private authService: AuthService, // ✅ Inject auth service
    private toastr: ToastrService,
    private alertService: AlertService // Inject AlertService
  ) { }

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userId = user.id;
    }
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data;
      if (this.warehouses.length > 0) {
        this.selectedWarehouse = this.warehouses[0].id;
        this.loadInventoryForWarehouse(this.selectedWarehouse);
      }
    });
  }

  onSelect(event: any): void {
    this.selectedWarehouse = event.target.value;
    this.loadInventoryForWarehouse(this.selectedWarehouse);
  }

  loadInventoryForWarehouse(warehouseId: string): void {
    this.inventoryService.getInventoryForWarehouse(warehouseId).subscribe(data => {
      this.inventory = data;
      this.inventory.forEach(item => {
        if (item.quantity < item.minQuantity) {
          this.createLowStockAlert(item);
        } else {
          this.closeLowStockAlertIfExists(item);
        }
      });
    });

    this.warehouseService.getStorageLocationsForWarehouse(warehouseId).subscribe((locations: StorageLocation[]) => {
      this.availableLocations = locations;
    });
  }

  onToWarehouseChange(): void {
    if (this.moveToWarehouseId) {
      this.warehouseService.getStorageLocationsForWarehouse(this.moveToWarehouseId).subscribe((locations: StorageLocation[]) => {
        this.availableToLocations = locations;
      });
    } else {
      this.availableToLocations = [];
    }
  }

  openMoveModal(item: WarehouseInventory): void {
    console.log('Move modal opened for:', item);
    this.selectedItem = item;
    this.moveToWarehouseId = '';
    this.moveToLocationId = '';
    this.moveQuantity = 1;
    this.availableToLocations = [];
    this.showMoveModal = true;
  }

  closeMoveModal(): void {
    this.showMoveModal = false;
    this.selectedItem = null;
  }

  confirmMove(): void {

    if (!this.selectedItem || !this.moveToWarehouseId || !this.moveToLocationId || this.moveQuantity < 1 || !this.userId) {
      console.warn('Missing required fields for transfer');
      return;
    }

    const dto: MoveInventoryDTO = {
      fromWarehouseId: this.selectedItem.warehouseId,
      toWarehouseId: this.moveToWarehouseId,
      itemId: this.selectedItem.itemId,
      fromLocationId: this.selectedItem.storageLocationId,
      toLocationId: this.moveToLocationId,
      quantity: this.moveQuantity,
      userId: this.userId
    };

    console.log('DTO being sent:', dto);

    this.inventoryService.moveInventory(dto).subscribe({
      next: () => {
        this.toastr.success('Inventory moved successfully!', 'Success');
        this.closeMoveModal();
        this.loadInventoryForWarehouse(this.selectedWarehouse);
      },
      error: (err) => {
        this.toastr.error('Failed to move inventory.', 'Error');
        console.error('Move failed:', err);
      }
    });
  }

  getWarehouseNameById(id: string | undefined): string {
    if (!id) return '';
    const match = this.warehouses.find(w => w.id === id);
    return match?.name || id;
  }

  createLowStockAlert(item: WarehouseInventory) {
    const message = `Low stock: ${item.itemName || item.itemId} at ${item.storageLocationName || item.storageLocationId}. Reorder.`;
    const alert = {
      message,
      status: 'NEW',
      warehouseId: item.warehouseId,
      assignedUserId: null
    };
    this.alertService.createAlert(alert).subscribe({
      next: () => {
        this.toastr.info(`Alert created for ${item.itemName || item.itemId} at ${item.storageLocationName || item.storageLocationId}`);
      },
      error: () => {
        this.toastr.error('Failed to create low stock alert.');
      }
    });
  }

  closeLowStockAlertIfExists(item: WarehouseInventory) {
    this.alertService.getAllAlerts().subscribe(alerts => {
      const alert = alerts.find(a =>
        a.status === 'NEW' &&
        a.message &&
        a.message.includes(item.itemName || item.itemId) &&
        a.message.includes(item.storageLocationName || item.storageLocationId)
      );
      if (alert) {
        this.alertService.updateAlertStatus(alert.id, 'CLOSED').subscribe({
          next: () => {
            this.toastr.success(`Low stock alert closed for ${item.itemName || item.itemId} at ${item.storageLocationName || item.storageLocationId}`);
          },
          error: () => {
            this.toastr.error('Failed to close low stock alert.');
          }
        });
      }
    });
  }
}
