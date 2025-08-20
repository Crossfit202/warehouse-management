import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  currentUser: any = {};
  editUser: any = {};
  isEditMode = false;
  avatarColor: string = '#6c63ff';

  constructor(private userService: UserService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(user => {
      this.currentUser = user;
    });
    this.avatarColor = this.getRandomColor();
  }

  getRandomColor(): string {
    // Generates a random pastel color
    const hue = Math.floor(Math.random() * 360);
    return `hsl(${hue}, 70%, 70%)`;
  }

  startEdit() {
    this.editUser = { ...this.currentUser };
    this.isEditMode = true;
  }

  cancelEdit() {
    this.isEditMode = false;
  }

  saveEdit() {
    // Password change validation
    if (this.editUser.newPassword || this.editUser.confirmPassword) {
      if (!this.editUser.currentPassword) {
        this.toastr.error('Please enter your current password.');
        return;
      }
      if (this.editUser.newPassword !== this.editUser.confirmPassword) {
        this.toastr.error('New passwords do not match.');
        return;
      }
    }

    // Prepare payload
    const payload: any = {
      id: this.editUser.id,
      username: this.editUser.username,
      email: this.editUser.email,
      role: this.editUser.role
    };

    // Only include password fields if changing password
    if (this.editUser.newPassword) {
      payload.currentPassword = this.editUser.currentPassword;
      payload.newPassword = this.editUser.newPassword;
    }

    this.userService.updateUser(payload).subscribe({
      next: (updated) => {
        this.currentUser = updated;
        this.isEditMode = false;
        this.toastr?.success('Profile updated!');
      },
      error: (err) => {
        this.toastr?.error('Failed to update profile.');
      }
    });
  }
}
