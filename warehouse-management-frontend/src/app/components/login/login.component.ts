import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  message = '';
  showErrorModal = false; // Add this property

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  login() {
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        if (response && response.userId) {
          this.message = 'Login successful!';
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
          this.router.navigate([returnUrl]);
        } else {
          this.message = response?.message || 'Invalid username and/or password. Please try again';
        }
      },
      error: (error) => {
        console.log('Login Error:', error);
        this.showErrorModal = true;
        this.message =
          error.error?.error ||
          error.error?.message ||
          error.message ||
          'Invalid username and/or password. Please try again';
      }
    });
  }

  closeErrorModal() {
    this.showErrorModal = false;
  }

  // OAuth 
  loginWithGoogle() {
    window.location.href = 'http://localhost:8083/oauth2/authorization/google';
  }

  loginWithGitHub() {
    window.location.href = 'http://localhost:8083/oauth2/authorization/github';
  }

}
