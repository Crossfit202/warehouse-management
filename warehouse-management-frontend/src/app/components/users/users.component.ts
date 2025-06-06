import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';

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

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe(data => {
      this.users = data.map(user => new User(
        user.id,
        user.username,
        user.email,
        user.password,
        user.role,
        user.created_at,
        user.updated_at
      ));
    });
  }

  openEditModal(user: User): void {
    this.selectedUser = new User(
      user.id,
      user.username,
      user.email,
      user.password,
      user.role,
      user.created_at,
      user.updated_at
    );

    this.isEditModalOpen = true;
  }

  closeEditModal(): void {
    this.selectedUser = null;
    this.isEditModalOpen = false;
  }

  saveUserChanges(): void {
    if (!this.selectedUser) return;

    // Create a shallow copy and remove the password field
    const { password, ...userWithoutPassword } = this.selectedUser;

    this.userService.updateUser(userWithoutPassword as User).subscribe({
      next: (updated) => {
        const index = this.users.findIndex(u => u.id === this.selectedUser!.id);
        if (index !== -1) {
          this.users[index] = updated;
        }
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
}
