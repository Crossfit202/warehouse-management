import { Component, OnInit } from '@angular/core';
import { MovementService } from '../../services/movement.service';
import { WarehouseService } from '../../services/warehouse.service';
import { InventoryMovement } from '../../models/InventoryMovement';
import { Warehouse } from '../../models/Warehouse';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-movements',
  templateUrl: './movements.component.html',
  styleUrls: ['./movements.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class MovementsComponent implements OnInit {
  movements: InventoryMovement[] = [];
  warehouses: Warehouse[] = [];
  selectedWarehouseId: string = '';

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
}
