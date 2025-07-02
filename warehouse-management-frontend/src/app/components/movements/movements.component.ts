import { Component, OnInit } from '@angular/core';
import { MovementService } from '../../services/movement.service';
import { WarehouseService } from '../../services/warehouse.service';
import { InventoryMovement } from '../../models/InventoryMovement';
import { Warehouse } from '../../models/Warehouse';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-movements',
  templateUrl: './movements.component.html',
  styleUrls: ['./movements.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class MovementsComponent implements OnInit {
  movements: InventoryMovement[] = [];
  warehouses: Warehouse[] = [];
  selectedWarehouseId: string = '';
  searchTerm: string = '';
  sortField: 'itemName' | 'quantity' | 'movementType' | 'time' = 'time';
  sortDirection: 'asc' | 'desc' = 'desc';

  constructor(
    private movementService: MovementService,
    private warehouseService: WarehouseService
  ) { }

  ngOnInit(): void {
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data;
      if (this.warehouses.length > 0) {
        this.selectedWarehouseId = this.warehouses[0].id;
        this.loadMovements(this.selectedWarehouseId);
      }
    });
  }

  onWarehouseSelect(event: any): void {
    this.selectedWarehouseId = event.target.value;
    this.loadMovements(this.selectedWarehouseId);
  }

  loadMovements(warehouseId: string): void {
    this.movementService.getMovementsForWarehouse(warehouseId).subscribe(data => {
      this.movements = data;
    });
  }

  get filteredMovements(): InventoryMovement[] {
    if (!this.searchTerm.trim()) return this.movements;
    const term = this.searchTerm.trim().toLowerCase();
    return this.movements.filter(m =>
      (m.itemName?.toLowerCase().includes(term) ||
       m.fromWarehouseName?.toLowerCase().includes(term) ||
       m.toWarehouseName?.toLowerCase().includes(term) ||
       m.movementType?.toLowerCase().includes(term) ||
       m.userName?.toLowerCase().includes(term) ||
       m.quantity?.toString().includes(term) ||
       (m.time && new Date(m.time).toLocaleString().toLowerCase().includes(term))
      )
    );
  }

  setSort(field: 'itemName' | 'quantity' | 'movementType' | 'time') {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = field === 'time' ? 'desc' : 'asc';
    }
  }

  get sortedMovements(): InventoryMovement[] {
    const arr = [...this.filteredMovements];
    arr.sort((a, b) => {
      let aVal: any, bVal: any;
      if (this.sortField === 'quantity') {
        aVal = a.quantity ?? 0;
        bVal = b.quantity ?? 0;
      } else if (this.sortField === 'time') {
        aVal = new Date(a.time).getTime();
        bVal = new Date(b.time).getTime();
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
}
