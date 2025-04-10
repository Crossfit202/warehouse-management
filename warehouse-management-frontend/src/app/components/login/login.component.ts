import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
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

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  login() {
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        console.log('Backend response:', response);
        this.message = response.message || 'Login successful!';
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
        this.router.navigate([returnUrl]);
      },
      error: (error) => {
        console.error('Login error:', error);
        this.message = error.error?.message || 'Login failed. Please try again.';
      }
    });
  }

  // OAuth 
  loginWithGoogle() {
    window.location.href = 'http://localhost:8083/oauth2/authorization/google';
  }

  loginWithGitHub() {
    window.location.href = 'http://localhost:8083/oauth2/authorization/github';
  }

}
