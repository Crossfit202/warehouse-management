import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';
import { WarehousePersonnelService, WarehousePersonnelDTO } from '../../services/personnel.service';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { PersonnelStatusEnum } from '../../models/PersonnelStatusEnum';
import { ToastrService } from 'ngx-toastr'; // <-- Add this import

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[] = [];
  selectedUser: User | null = null;
  isEditModalOpen: boolean = false;

  warehouses: Warehouse[] = [];
  userAssignments: { [userId: string]: WarehousePersonnelDTO | null } = {};
  selectedAssignment: WarehousePersonnelDTO | null = null;

  constructor(
    private userService: UserService,
    private warehousePersonnelService: WarehousePersonnelService,
    private warehouseService: WarehouseService,
    private toastr: ToastrService // <-- Inject ToastrService
  ) { }

  ngOnInit(): void {
    this.loadUsers();
    this.loadWarehouses();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe(users => {
      this.users = users;
      // For each user, fetch their warehouse assignment
      users.forEach(user => {
        this.warehousePersonnelService.getWarehousesForUser(user.id).subscribe(assignments => {
          console.log('Assignments for user', user.id, assignments);
          this.userAssignments[user.id] = assignments[0] || null;
        });
      });
    });
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe(data => {
      this.warehouses = data;
    });
  }

  openEditModal(user: User): void {
    this.selectedUser = new User(
      user.id, user.username, user.email, user.password, user.role, user.created_at, user.updated_at
    );
    this.isEditModalOpen = true;
    this.warehousePersonnelService.getWarehousesForUser(user.id).subscribe(assignments => {
      const active = assignments.find(a => a.status === PersonnelStatusEnum.ACTIVE);
      this.selectedAssignment = active || assignments[0] || {
        id: '',
        userId: user.id,
        username: user.username,
        warehouseId: '',
        warehouseName: '',
        status: PersonnelStatusEnum.UNASSIGNED
      };
    });
  }

  closeEditModal(): void {
    this.selectedUser = null;
    this.isEditModalOpen = false;
  }

  saveUserChanges(): void {
    if (!this.selectedUser) return;

    const { password, ...userWithoutPassword } = this.selectedUser;
    this.userService.updateUser(userWithoutPassword as User).subscribe({
      next: (updated) => {
        const index = this.users.findIndex(u => u.id === this.selectedUser!.id);
        if (index !== -1) {
          this.users[index] = updated;
        }
        this.toastr.success('User updated successfully!');
        this.saveWarehouseAssignment(() => {
          this.loadUsers();
          this.closeEditModal();
        });
      },
      error: (err) => {
        this.toastr.error('Failed to update user.');
        console.error('Failed to update user:', err);
      }
    });
  }

  deleteUser(userId: string): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          this.users = this.users.filter(u => u.id !== userId);
          this.toastr.success('User deleted successfully!');
        },
        error: (err) => {
          this.toastr.error('Failed to delete user.');
          console.error('Failed to delete user:', err);
        }
      });
    }
  }

  saveWarehouseAssignment(afterSave?: () => void): void {
    if (!this.selectedAssignment) {
      if (afterSave) afterSave();
      return;
    }

    if (!this.selectedAssignment.warehouseId) {
      // If "Unassigned" is selected and there is an assignment, delete it
      if (this.selectedAssignment.id) {
        this.warehousePersonnelService.deleteWarehousePersonnel(this.selectedAssignment.id)
          .subscribe({
            next: () => {
              this.selectedAssignment = {
                id: '',
                userId: this.selectedUser!.id,
                username: this.selectedUser!.username,
                warehouseId: '',
                warehouseName: '',
                status: PersonnelStatusEnum.UNASSIGNED
              };
              this.toastr.success('Warehouse assignment removed.');
              if (afterSave) afterSave();
            },
            error: (err) => {
              this.toastr.error('Failed to remove warehouse assignment.');
              if (afterSave) afterSave();
            }
          });
      } else {
        if (afterSave) afterSave();
      }
    } else if (this.selectedAssignment.id) {
      // Update existing assignment
      this.warehousePersonnelService.updateWarehousePersonnel(this.selectedAssignment.id, this.selectedAssignment)
        .subscribe({
          next: () => {
            this.toastr.success('Warehouse assignment updated.');
            if (afterSave) afterSave();
          },
          error: (err) => {
            this.toastr.error('Failed to update warehouse assignment.');
            if (afterSave) afterSave();
          }
        });
    } else if (this.selectedAssignment.warehouseId) {
      // Create new assignment
      if (!this.selectedAssignment.status) {
        this.selectedAssignment.status = PersonnelStatusEnum.ACTIVE;
      }
      this.warehousePersonnelService.createWarehousePersonnel(this.selectedAssignment)
        .subscribe({
          next: () => {
            this.toastr.success('Warehouse assignment created.');
            if (afterSave) afterSave();
          },
          error: (err) => {
            this.toastr.error('Failed to create warehouse assignment.');
            if (afterSave) afterSave();
          }
        });
    } else {
      if (afterSave) afterSave();
    }
  }
  PersonnelStatusEnum = PersonnelStatusEnum; // for template access
}
