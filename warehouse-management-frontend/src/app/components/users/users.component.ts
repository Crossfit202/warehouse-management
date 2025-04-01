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

}
