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

  editingAlert: any = null;

  deleteConfirmAlertId: string | null = null;

  editAlert: Alert | null = null;
  editPersonnel: User[] = [];

  sortField: 'time' | 'status' = 'time';
  sortDirection: 'asc' | 'desc' = 'desc';

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
          dto.id,
          dto.username,
          dto.email || '',
          dto.role || '',
          dto.firstName || '',
          dto.lastName || '',
          dto.active ?? true
        );
      });
    });
  }

  // method to handle drop-down list change
  onSelect(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedWarehouse = select.value;
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

  openDeleteModal(alertId: string): void {
    this.deleteConfirmAlertId = alertId;
  }

  closeDeleteModal(): void {
    this.deleteConfirmAlertId = null;
  }

  confirmDelete(): void {
    if (this.deleteConfirmAlertId) {
      this.alertService.deleteAlert(this.deleteConfirmAlertId).subscribe({
        next: () => {
          this.toastr.success('Alert deleted successfully!', 'Success');
          this.loadAlerts();
          this.closeDeleteModal();
        },
        error: () => {
          this.toastr.error('Failed to delete alert.', 'Error');
          this.closeDeleteModal();
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
    // Clone the alert to avoid mutating the list directly
    this.editAlert = new Alert(
      alert.id,
      alert.warehouse,
      alert.message,
      alert.status,
      alert.time,
      alert.assignedUserId,
      alert.assignedUserName
    );
    // Load users for the alert's warehouse
    if (alert.warehouse && alert.warehouse.id) {
      this.userService.getUsersForWarehouse(alert.warehouse.id).subscribe((dtos: WarehousePersonnelDTO[]) => {
        this.editPersonnel = dtos.map(dto => new User(
          dto.userId,
          dto.username,
          '', '', '', '', ''
        ));
      });
    } else {
      this.editPersonnel = [];
    }
  }

  closeEditModal(): void {
    this.editAlert = null;
    this.editPersonnel = [];
  }

  saveEdit(): void {
    if (!this.editAlert) return;
    const dto = {
      id: this.editAlert.id,
      message: this.editAlert.message,
      status: this.editAlert.status,
      warehouseId: this.editAlert.warehouse?.id,
      assignedUserId: this.editAlert.assignedUserId || null
    };
    this.alertService.updateAlert(dto).subscribe({
      next: () => {
        this.toastr.success('Alert updated successfully!');
        this.closeEditModal();
        this.loadAlerts();
      },
      error: () => {
        this.toastr.error('Failed to update alert.');
      }
    });
  }

  setSort(field: 'time' | 'status') {
    if (this.sortField === field) {
      // Toggle direction if clicking the same field
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = field === 'time' ? 'desc' : 'asc'; // default: newest first, status asc
    }
  }

  get sortedAlerts(): Alert[] {
    const arr = [...this.filteredAlerts];
    if (this.sortField === 'time') {
      arr.sort((a, b) => {
        const at = new Date(a.time).getTime();
        const bt = new Date(b.time).getTime();
        return this.sortDirection === 'asc' ? at - bt : bt - at;
      });
    } else if (this.sortField === 'status') {
      arr.sort((a, b) => {
        const order = { NEW: 1, ACTIVE: 2, CLOSED: 3 };
        const aOrder = order[a.status as keyof typeof order] ?? 99;
        const bOrder = order[b.status as keyof typeof order] ?? 99;
        const cmp = aOrder - bOrder;
        return this.sortDirection === 'asc' ? cmp : -cmp;
      });
    }
    return arr;
  }

  get filteredAlerts(): Alert[] {
    if (!this.selectedWarehouse) {
      return this.alerts;
    }
    const selected = this.warehouses.find(w => w.name === this.selectedWarehouse);
    if (!selected) return this.alerts;
    return this.alerts.filter(alert => alert.warehouse?.id === selected.id);
  }
}
