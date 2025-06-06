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
    private toastr: ToastrService
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


}
