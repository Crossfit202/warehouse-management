import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { LoginComponent } from './components/login/login.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LoginComponent],
  template: `
    <app-login></app-login>
    <button (click)="logout()">Logout</button>
    <button (click)="getUsers()">Get Users</button>
    <p>{{ message }}</p>
  `
})
export class AppComponent {
  message = '';

  constructor(private authService: AuthService) { }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.message = 'Logout successful!';
      },
      error: (error) => {
        this.message = 'Logout failed: ' + (error.error || error.message);
      }
    });
  }

  getUsers() {
    this.authService.getUsers().subscribe({
      next: (data) => {
        this.message = `Users: ${JSON.stringify(data)}`;
      },
      error: (error) => {
        this.message = 'Failed to get users: ' + (error.error || error.message);
      }
    });
  }
}
