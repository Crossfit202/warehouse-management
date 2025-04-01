import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {

  users: User[] = [];

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
      console.log('Created At Type:', typeof this.users[0]?.created_at, 'Value:', this.users[0]?.created_at);

    });
  }

  editUser(user: User): void {
    const updatedUsername = prompt('Edit username', user.username);
    if (updatedUsername && updatedUsername !== user.username) {
      const updatedUser: Partial<User> = { username: updatedUsername };
      this.userService.updateUser(user.id, updatedUser).subscribe({
        next: (updated) => {
          user.username = updated.username;
          console.log('User updated successfully:', updated);
        },
        error: (err) => console.error('Failed to update user:', err)
      });
    }
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
