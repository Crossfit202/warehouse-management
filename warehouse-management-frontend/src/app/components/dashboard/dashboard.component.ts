import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../services/alert.service';
import { Alert } from '../../models/Alert';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{

  alerts: Alert[] = [];
  warehouses: Warehouse[] = [];   // an array for the warehouses
  selectedWarehouse: string = ''; // property to hold the warehouse a user selects

    constructor(private alertService: AlertService, private warehouseService: WarehouseService) {}


    ngOnInit(): void {
      this.loadAlerts();
      this.loadWarehouses();
    }

    loadAlerts(): void {
      this.alertService.getAllAlerts().subscribe(data => {
        this.alerts = data;
      })
    }

    loadWarehouses(): void {
      this.warehouseService.getAllWarehouses().subscribe(data => {
        this.warehouses = data;
        
        // input the first warehouse name to selectedWarehouse
        if (this.warehouses.length > 0) {
          this.selectedWarehouse = this.warehouses[0].name;
        }
    
      });  
    }

    // method to handle drop-down list change
    onSelect(event: Event): void{
      const input = event.target as HTMLInputElement;
      this.selectedWarehouse = input.value;
    }

}
