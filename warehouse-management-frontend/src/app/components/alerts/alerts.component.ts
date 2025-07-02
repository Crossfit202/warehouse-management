import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Alert } from '../../models/Alert';
import { AlertService } from '../../services/alert.service';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { UserService } from '../../services/user.service';
import { User } from '../../models/User';
import { FormsModule } from '@angular/forms'; // <-- Add this import
import { ToastrService } from 'ngx-toastr';
import { WarehousePersonnelDTO } from '../../services/personnel.service';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [CommonModule, FormsModule], // <-- Add FormsModule here
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.css'
})
export class AlertsComponent implements OnInit {

  alerts: Alert[] = []; // an array for the alerts
  warehouses: Warehouse[] = [];   // an array for the warehouses
  users: User[] = []; // an array for the users
  selectedWarehouse: string = ''; // property to hold the warehouse a user selects

  newAlert = {
    message: '',
    status: 'NEW',
    warehouseId: '',
    assignedUserId: ''
  };

  personnelForSelectedWarehouse: User[] = [];
  personnelForAlertWarehouse: { [alertId: string]: User[] } = {};

  constructor(
    private alertService: AlertService,
    private warehouseService: WarehouseService,
    private userService: UserService, // <-- Inject UserService
    private toastr: ToastrService // <-- Inject ToastrService
  ) { }

  ngOnInit(): void {
    this.loadAlerts();
    this.loadWarehouses();
    this.loadUsers(); // <-- Load users
  }

  loadAlerts(): void {
    this.alertService.getAllAlerts().subscribe(
      data => {
        this.alerts = data.map((obj: any) => new Alert(
          obj.id,
          obj.warehouse,
          obj.message,
          obj.status,
          obj.time,
          obj.assignedUserId,
          obj.assignedUserName
        ));
        // For each alert, load personnel for its warehouse
        this.alerts.forEach(alert => {
          if (alert.warehouse && alert.warehouse.id) {
            this.userService.getUsersForWarehouse(alert.warehouse.id).subscribe((dtos: WarehousePersonnelDTO[]) => {
              this.personnelForAlertWarehouse[alert.id] = dtos.map(dto => new User(
                dto.userId, // <-- use userId, not id!
                dto.username,
                '',
                '',
                '',
                '',
                ''
              ));
            });
          }
        });
      }
    );
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

  loadUsers(): void {
    this.userService.getAllUsers().subscribe(data => {
      this.users = data;
    });
  }

  loadUsersForWarehouse(warehouseId: string): void {
    this.userService.getUsersForWarehouse(warehouseId).subscribe(data => {
      // Map WarehousePersonnelDTO[] to User[]
      this.users = data.map((dto: any) => {
        // Adjust the mapping as per the User constructor's parameter order
        return new User(
          dto.id,               // id
          dto.username,         // username
          dto.email || '',      // email (provide default if missing)
          dto.role || '',       // role (provide default if missing)
          dto.firstName || '',  // firstName (provide default if missing)
          dto.lastName || '',   // lastName (provide default if missing)
          dto.active ?? true    // active (provide default if missing)
        );
      });
    });
  }

  // method to handle drop-down list change
  onSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedWarehouse = input.value;
    const selected = this.warehouses.find(w => w.name === this.selectedWarehouse);
    if (selected) {
      this.loadUsersForWarehouse(selected.id);
    }
  }

  // When warehouse is selected in the add alert form
  onNewAlertWarehouseChange(warehouseId: string): void {
    this.newAlert.warehouseId = warehouseId;
    this.newAlert.assignedUserId = '';
    // For add alert form
    this.userService.getUsersForWarehouse(warehouseId).subscribe((dtos: WarehousePersonnelDTO[]) => {
      this.personnelForSelectedWarehouse = dtos.map(dto => new User(
        dto.userId,
        dto.username,
        '', '', '', '', ''
      ));
    });
  }

  saveAlert(alert: Alert): void {
    // Convert empty string to null for unassigned user
    const dto = {
      id: alert.id,
      message: alert.message,
      status: alert.status,
      warehouseId: alert.warehouse?.id,
      assignedUserId: alert.assignedUserId || null
    };
    this.alertService.updateAlert(dto).subscribe({
      next: updated => {
        this.toastr.success('Alert updated successfully!');
      },
      error: err => {
        this.toastr.error('Failed to update alert.');
      }
    });
  }

  deleteAlert(alertId: string): void {
    if (confirm('Are you sure you want to delete this alert?')) {
      this.alertService.deleteAlert(alertId).subscribe({
        next: () => {
          this.toastr.success('Alert deleted successfully!', 'Success');
          this.loadAlerts(); // Refresh the list
        },
        error: () => {
          this.toastr.error('Failed to delete alert.', 'Error');
        }
      });
    }
  }

  addAlert(): void {
    if (!this.newAlert.message || !this.newAlert.status || !this.newAlert.warehouseId) {
      this.toastr.error('Please fill out all fields.');
      return;
    }
    const alertToSend = {
      message: this.newAlert.message,
      status: this.newAlert.status,
      warehouseId: this.newAlert.warehouseId,
      assignedUserId: this.newAlert.assignedUserId || null
    };
    console.log('Sending alert:', alertToSend);
    this.alertService.createAlert(alertToSend).subscribe({
      next: () => {
        this.toastr.success('Alert created successfully!');
        this.newAlert = { message: '', status: 'NEW', warehouseId: '', assignedUserId: '' };
        this.personnelForSelectedWarehouse = [];
        this.loadAlerts();
      },
      error: () => {
        this.toastr.error('Failed to create alert.');
      }
    });
  }

  get safePersonnelForSelectedWarehouse(): User[] {
    return this.personnelForSelectedWarehouse || [];
  }

  getAssignedUserName(alert: Alert): string {
    return alert.assignedUserName || 'Unassigned';
  }

  openEditModal(alert: Alert): void {
    // Placeholder for modal logic
    // You can implement modal opening here later
    console.log('Edit modal opened for alert:', alert);
  }
}
