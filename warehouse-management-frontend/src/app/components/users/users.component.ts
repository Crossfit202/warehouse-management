import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';
import { WarehousePersonnelService, WarehousePersonnelDTO } from '../../services/personnel.service';
import { WarehouseService } from '../../services/warehouse.service';
import { Warehouse } from '../../models/Warehouse';
import { PersonnelStatusEnum } from '../../models/PersonnelStatusEnum';

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
    private warehouseService: WarehouseService
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

    // Save user info
    const { password, ...userWithoutPassword } = this.selectedUser;
    this.userService.updateUser(userWithoutPassword as User).subscribe({
      next: (updated) => {
        const index = this.users.findIndex(u => u.id === this.selectedUser!.id);
        if (index !== -1) {
          this.users[index] = updated;
        }
        // Save assignment/status as well!
        this.saveWarehouseAssignment();
        this.closeEditModal();
      },
      error: (err) => console.error('Failed to update user:', err)
    });
  }

  deleteUser(userId: string): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          this.users = this.users.filter(u => u.id !== userId);
          console.log('User deleted');
        },
        error: (err) => console.error('Failed to delete user:', err)
      });
    }
  }

  saveWarehouseAssignment(): void {
    if (!this.selectedAssignment) return;

    if (!this.selectedAssignment.warehouseId) {
      // If "Unassigned" is selected and there is an assignment, delete it
      if (this.selectedAssignment.id) {
        this.warehousePersonnelService.deleteWarehousePersonnel(this.selectedAssignment.id)
          .subscribe(() => {
            this.selectedAssignment = {
              id: '',
              userId: this.selectedUser!.id,
              username: this.selectedUser!.username,
              warehouseId: '',
              warehouseName: '',
              status: PersonnelStatusEnum.UNASSIGNED
            };
          });
      }
    } else if (this.selectedAssignment.id) {
      // Update existing assignment
      console.log('Updating assignment:', this.selectedAssignment);
      this.warehousePersonnelService.updateWarehousePersonnel(this.selectedAssignment.id, this.selectedAssignment)
        .subscribe(() => this.loadUsers());
    } else if (this.selectedAssignment.warehouseId) {
      // Create new assignment
      // If status is not set, default to ACTIVE
      if (!this.selectedAssignment.status) {
        this.selectedAssignment.status = PersonnelStatusEnum.ACTIVE;
      }
      this.warehousePersonnelService.createWarehousePersonnel(this.selectedAssignment)
        .subscribe();
    }
  }
  PersonnelStatusEnum = PersonnelStatusEnum; // for template access
}
