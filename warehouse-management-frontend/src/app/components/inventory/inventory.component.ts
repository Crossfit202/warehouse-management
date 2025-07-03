import { Component, OnInit } from '@angular/core';
import { InventoryService } from '../../services/inventory.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WarehouseService } from '../../services/warehouse.service';
import { StorageLocation } from '../../models/StorageLocation';
import { WarehouseInventory } from '../../models/WarehouseInventory';
import { MoveInventoryDTO } from '../../models/MoveInventoryDTO';
import { AddInventoryDTO } from '../../models/AddInventoryDTO';
import { AuthService } from '../../services/auth.service'; // ✅ To get logged-in user
import { ToastrService } from 'ngx-toastr';
import { AlertService } from '../../services/alert.service'; // Import your alert service
import { InventoryItemService } from '../../services/inventory-item.service'; // <-- Add this import

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
  allWarehouses: any[] = []; // Store all warehouses
  selectedWarehouse: string = '';

  // Modal State
  showMoveModal: boolean = false;
  showAddModal: boolean = false;
  showDeleteModal: boolean = false;
  showDeleteAllModal: boolean = false;
  selectedItem: WarehouseInventory | null = null;
  moveToWarehouseId: string = '';
  moveToLocationId: string = '';
  moveQuantity: number = 1;
  deleteQuantity: number = 1;
  itemToDelete: WarehouseInventory | null = null;
  availableLocations: StorageLocation[] = [];
  availableToLocations: StorageLocation[] = [];
  products: any[] = [];
  newInventoryItem: any = {};

  userId: string = ''; // ✅ For logging movement
  minQuantityDisabled: boolean = false; // Add this line

  // Search and Sort
  searchTerm: string = '';
  sortField: 'itemName' | 'storageLocationName' | 'quantity' | 'status' | 'warehouse' = 'itemName';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private inventoryService: InventoryService,
    private warehouseService: WarehouseService,
    private inventoryItemService: InventoryItemService, // <-- Inject here
    private authService: AuthService, // ✅ Inject auth service
    private toastr: ToastrService,
    private alertService: AlertService // Inject AlertService
  ) { }

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.userId = user?.id || ''; // <-- Add this line
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.allWarehouses = data; // Save all warehouses for transfer modal

      if (this.isAdmin) {
        this.warehouses = data;
        this.selectedWarehouse = '';
        this.loadAllInventory();
      } else {
        // Only show warehouses where user is ACTIVE
        this.warehouses = data.filter(w =>
          w.personnel?.some((p: any) => (p.userId === user?.id || p.id === user?.id) && p.status === 'ACTIVE')
        );
        // Set default to first warehouse if available
        if (this.warehouses.length > 0) {
          this.selectedWarehouse = this.warehouses[0].id;
          this.loadInventoryForWarehouse(this.selectedWarehouse);
        } else {
          this.selectedWarehouse = '';
          this.inventory = [];
        }
      }
    });

    // Load products for the dropdown
    this.inventoryItemService.getAllItems().subscribe((items: any[]) => this.products = items);
  }

  onSelect(event: any): void {
    this.selectedWarehouse = event.target.value;
    if (!this.selectedWarehouse) {
      // All Warehouses selected
      this.loadAllInventory();
    } else {
      this.loadInventoryForWarehouse(this.selectedWarehouse);
    }
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

  loadAllInventory(): void {
    this.inventoryService.getAllInventory().subscribe(data => {
      this.inventory = data;
      this.inventory.forEach(item => {
        if (item.quantity < item.minQuantity) {
          this.createLowStockAlert(item);
        } else {
          this.closeLowStockAlertIfExists(item);
        }
      });
    });
    // Optionally clear availableLocations if you want to disable add for "All"
    this.availableLocations = [];
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

  openAddModal(): void {
    this.newInventoryItem = {
      warehouseId: this.selectedWarehouse // Set the currently selected warehouse
    };
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
  }

  // When warehouse changes, update available locations
  onAddWarehouseChange(): void {
    if (this.newInventoryItem.warehouseId) {
      this.warehouseService.getStorageLocationsForWarehouse(this.newInventoryItem.warehouseId)
        .subscribe(locations => {
          this.availableLocations = locations;
          // Optionally set the first location as default
          if (locations.length > 0) {
            this.newInventoryItem.storageLocationId = locations[0].id;
          }
        });
    } else {
      this.availableLocations = [];
      this.newInventoryItem.storageLocationId = null;
    }
  }

  onProductOrLocationChange(): void {
    if (!this.newInventoryItem.itemId || !this.newInventoryItem.storageLocationId || !this.newInventoryItem.warehouseId) {
      this.minQuantityDisabled = false;
      return;
    }
    const existing = this.inventory.find(
      item =>
        item.itemId === this.newInventoryItem.itemId &&
        item.storageLocationId === this.newInventoryItem.storageLocationId &&
        item.warehouseId === this.newInventoryItem.warehouseId
    );
    if (existing && existing.minQuantity != null) {
      this.newInventoryItem.minQuantity = existing.minQuantity;
      this.minQuantityDisabled = false; // Allow editing!
    } else {
      this.newInventoryItem.minQuantity = null;
      this.minQuantityDisabled = false;
    }
  }

  addInventoryItem(): void {
    if (!this.newInventoryItem.storageLocationId) {
      this.toastr.error('Please select a storage location.');
      return;
    }

    // Find the selected storage location's max capacity
    const selectedLocation = this.availableLocations.find(
      loc => loc.id === this.newInventoryItem.storageLocationId
    );
    const maxCapacity = selectedLocation ? selectedLocation.max_capacity : null;

    // Calculate the total quantity in the storage location (all products)
    const totalCurrentQty = this.inventory
      .filter(item =>
        item.storageLocationId === this.newInventoryItem.storageLocationId &&
        item.warehouseId === this.newInventoryItem.warehouseId
      )
      .reduce((sum, item) => sum + (item.quantity || 0), 0);

    const newQty = Number(this.newInventoryItem.quantity);
    const totalQtyAfterAdd = totalCurrentQty + newQty;

    if (maxCapacity !== null && totalQtyAfterAdd > maxCapacity) {
      this.toastr.error(`Cannot add inventory: would exceed max capacity (${maxCapacity}) for this location.`);
      return;
    }

    // Check if the item already exists in the selected warehouse/location
    const existingItem = this.inventory.find(
      item =>
        item.itemId === this.newInventoryItem.itemId &&
        item.storageLocationId === this.newInventoryItem.storageLocationId &&
        item.warehouseId === this.newInventoryItem.warehouseId
    );

    if (!existingItem && !this.newInventoryItem.minQuantity) {
      this.toastr.error('Please enter a minimum quantity.');
      return;
    }

    if (existingItem) {
      const updatedItem: any = {
        ...existingItem,
        quantity: existingItem.quantity + Number(this.newInventoryItem.quantity),
        userId: this.userId // <-- Ensure userId is included
      };
      if (!this.minQuantityDisabled && this.newInventoryItem.minQuantity != null && this.newInventoryItem.minQuantity !== '') {
        updatedItem.minQuantity = Number(this.newInventoryItem.minQuantity);
      }
      this.inventoryService.editInventoryItem(updatedItem).subscribe({
        next: () => {
          this.closeAddModal();
          this.loadInventoryForWarehouse(this.selectedWarehouse);
          this.toastr.success('Inventory quantity updated.');
        },
        error: () => {
          this.toastr.error('Failed to update inventory.');
        }
      });
    } else {
      // Add as new inventory record
      const dto: AddInventoryDTO = {
        warehouseId: this.newInventoryItem.warehouseId,
        itemId: this.newInventoryItem.itemId,
        storageLocationId: this.newInventoryItem.storageLocationId,
        quantity: Number(this.newInventoryItem.quantity),
        minQuantity: Number(this.newInventoryItem.minQuantity),
        userId: this.userId
      };
      this.inventoryService.addInventoryItem(dto).subscribe({
        next: () => {
          this.closeAddModal();
          this.loadInventoryForWarehouse(this.selectedWarehouse);
          this.toastr.success('Inventory added.');
        },
        error: () => {
          this.toastr.error('Failed to add inventory.');
        }
      });
    }
  }

  confirmMove(): void {
    if (!this.selectedItem || !this.moveToWarehouseId || !this.moveToLocationId || this.moveQuantity < 1 || !this.userId) {
      console.warn('Missing required fields for transfer');
      return;
    }

    // Assign to local variable to satisfy TypeScript
    const selectedItem = this.selectedItem;

    this.inventoryService.getInventoryForWarehouse(this.moveToWarehouseId).subscribe(destInventory => {
      const toLocation = this.availableToLocations.find(loc => loc.id === this.moveToLocationId);
      if (!toLocation) {
        this.toastr.error('Receiving storage location not found.');
        return;
      }
      const maxCapacity = toLocation.max_capacity;

      const totalReceivingQty = destInventory
        .filter(item =>
          item.storageLocationId === this.moveToLocationId &&
          item.warehouseId === this.moveToWarehouseId
        )
        .reduce((sum, item) => sum + (item.quantity || 0), 0);

      const totalQtyAfterMove = totalReceivingQty + this.moveQuantity;

      if (maxCapacity !== null && totalQtyAfterMove > maxCapacity) {
        this.toastr.error(`Cannot transfer: would exceed max capacity (${maxCapacity}) for the receiving location.`);
        return;
      }

      // Use the local variable here
      const dto: MoveInventoryDTO = {
        fromWarehouseId: selectedItem.warehouseId,
        toWarehouseId: this.moveToWarehouseId,
        itemId: selectedItem.itemId,
        fromLocationId: selectedItem.storageLocationId,
        toLocationId: this.moveToLocationId,
        quantity: this.moveQuantity,
        userId: this.userId
      };

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
    });
  }

  getWarehouseNameById(id: string | undefined): string {
    if (!id) return '';
    const match = this.warehouses.find(w => w.id === id);
    return match?.name || id;
  }

  createLowStockAlert(item: WarehouseInventory) {
    // First, check if an alert already exists for this item/location with status NEW
    this.alertService.getAllAlerts().subscribe(alerts => {
      const exists = alerts.some(a =>
        a.status === 'NEW' &&
        a.message &&
        a.message.includes(item.itemName || item.itemId) &&
        a.message.includes(item.storageLocationName || item.storageLocationId)
      );
      if (!exists) {
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

  openDeleteModal(item: WarehouseInventory): void {
    if (this.isInvClerk) return;
    this.itemToDelete = item;
    this.deleteQuantity = 1;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.itemToDelete = null;
  }

  confirmDeleteQuantity(): void {
    if (this.isInvClerk) return;
    if (!this.itemToDelete || this.deleteQuantity < 1) return;
    if (this.deleteQuantity > this.itemToDelete.quantity) {
      this.toastr.error('Cannot remove more than available quantity.');
      return;
    }
    const newQuantity = this.itemToDelete.quantity - this.deleteQuantity;
    if (newQuantity === 0) {
      // Delete the inventory item
      this.inventoryService.deleteInventoryItem(this.itemToDelete.warehouseInventoryId, this.userId).subscribe({
        next: () => {
          this.closeDeleteModal();
          this.loadInventoryForWarehouse(this.selectedWarehouse);
          this.toastr.success('Inventory deleted.');
        },
        error: () => {
          this.toastr.error('Failed to delete inventory.');
        }
      });
    } else {
      // Update the quantity
      const updatedItem = {
        ...this.itemToDelete,
        quantity: newQuantity,
        userId: this.userId // <-- add this
      };
      this.inventoryService.editInventoryItem(updatedItem).subscribe({
        next: () => {
          this.closeDeleteModal();
          this.loadInventoryForWarehouse(this.selectedWarehouse);
          this.toastr.success('Inventory updated.');
        },
        error: () => {
          this.toastr.error('Failed to update inventory.');
        }
      });
    }
  }

  openDeleteAllModal(item: WarehouseInventory): void {
    if (this.isInvClerk) return;
    this.itemToDelete = item;
    this.showDeleteAllModal = true;
  }

  closeDeleteAllModal(): void {
    this.showDeleteAllModal = false;
    this.itemToDelete = null;
  }

  confirmDeleteAllQuantity(): void {
    if (this.isInvClerk) return;
    // check it's not null
    if (!this.itemToDelete) {
      console.warn('Error trying to delete all items: Item to delete is Null');
      return;
    }

    // Delete the inventory item
    this.inventoryService.deleteInventoryItem(this.itemToDelete.warehouseInventoryId, this.userId).subscribe({
      next: () => {
        this.closeDeleteAllModal();
        this.loadInventoryForWarehouse(this.selectedWarehouse);
        this.toastr.success('Inventory deleted.');
      },
      error: () => {
        this.toastr.error('Failed to delete inventory.');
      }
    });
  }

  saveMinQuantity(item: WarehouseInventory) {
    item.editingMinQuantity = false;
    // Exclude UI-only property before sending to backend
    const { editingMinQuantity, ...dto } = item;
    const updatedDto = { ...dto, userId: this.userId }; // <-- add userId properly
    this.inventoryService.editInventoryItem(updatedDto).subscribe({
      next: () => {
        this.toastr.success('Min quantity updated.');
        this.loadInventoryForWarehouse(this.selectedWarehouse);
      },
      error: () => {
        this.toastr.error('Failed to update min quantity.');
      }
    });
  }

  // Search and Sort
  get filteredInventory(): WarehouseInventory[] {
    if (!this.searchTerm.trim()) return this.inventory;
    const term = this.searchTerm.trim().toLowerCase();
    return this.inventory.filter(item =>
    (item.itemName?.toLowerCase().includes(term) ||
      item.itemId?.toLowerCase().includes(term) ||
      item.storageLocationName?.toLowerCase().includes(term) ||
      item.storageLocationId?.toLowerCase().includes(term) ||
      item.quantity?.toString().includes(term) ||
      item.minQuantity?.toString().includes(term))
    );
  }

  setSort(field: 'itemName' | 'storageLocationName' | 'quantity' | 'status' | 'warehouse') {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
  }

  get sortedInventory(): WarehouseInventory[] {
    const arr = [...this.filteredInventory];
    arr.sort((a, b) => {
      let aVal: any, bVal: any;
      if (this.sortField === 'quantity') {
        aVal = a.quantity ?? 0;
        bVal = b.quantity ?? 0;
      } else if (this.sortField === 'status') {
        const getStatus = (item: WarehouseInventory) =>
          item.quantity < item.minQuantity ? 'below' : 'sufficient';
        const order = { below: 1, sufficient: 2 };
        aVal = order[getStatus(a)];
        bVal = order[getStatus(b)];
      } else if (this.sortField === 'warehouse') {
        // Sort by warehouse name (fallback to warehouseId)
        aVal = this.getWarehouseNameById(a.warehouseId).toLowerCase();
        bVal = this.getWarehouseNameById(b.warehouseId).toLowerCase();
      } else {
        aVal = (a[this.sortField] || '').toString().toLowerCase();
        bVal = (b[this.sortField] || '').toString().toLowerCase();
      }
      if (aVal < bVal) return this.sortDirection === 'asc' ? -1 : 1;
      if (aVal > bVal) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });
    return arr;
  }

  isAllWarehousesSelected(): boolean {
    return !this.selectedWarehouse;
  }

  get isAdmin(): boolean {
    const user = this.authService.getCurrentUser();
    return user?.role === 'ROLE_ADMIN';
  }

  get isInvClerk(): boolean {
    const user = this.authService.getCurrentUser();
    return user?.role === 'ROLE_INV_CLERK';
  }
}
