import { Component, OnInit } from '@angular/core';
import { InventoryService } from '../../services/inventory.service';
import { InventoryItem } from '../../models/InventoryItem';
import { CommonModule } from '@angular/common';
import { WarehouseService } from '../../services/warehouse.service';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent implements OnInit {
  inventory: InventoryItem[] = [];
  warehouses: any[] = [];
  selectedWarehouse: string = '';

  constructor(
    private inventoryService: InventoryService,
    private warehouseService: WarehouseService
  ) {}

  ngOnInit(): void {
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data;
      console.log('Warehouses:', this.warehouses);

      if (this.warehouses.length > 0) {
        this.selectedWarehouse = this.warehouses[0].name;
        this.loadInventoryForWarehouse(this.selectedWarehouse); // ✅ Load initial inventory
      }
    });
  }

  onSelect(event: any): void {
    console.log('Selected warehouse:', event.target.value);
    this.selectedWarehouse = event.target.value;
    this.loadInventoryForWarehouse(this.selectedWarehouse);
  }

  loadInventoryForWarehouse(warehouseName: string): void {
    this.inventoryService.getInventoryForWarehouse(warehouseName).subscribe(data => {
      this.inventory = data;
    });
  }
}
