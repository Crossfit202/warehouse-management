import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  message = '';

  constructor(private authService: AuthService) { }

  login() {
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        console.log('Backend response:', response);
        this.message = response.message || 'Login successful!';
        // Optionally store token if needed
        if (response.token) {
          console.log('JWT Token:', response.token);
        }
      },
      error: (error) => {
        console.error('Login error:', error);
        if (error.status === 401) {
          this.message = 'Invalid username or password.';
        } else if (error.status === 500) {
          this.message = 'Server error. Please try again later.';
        } else {
          this.message = 'Login failed: ' + (error.error?.message || error.message);
        }
      }


    });
  }
}
