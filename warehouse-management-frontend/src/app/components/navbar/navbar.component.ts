import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  username: string | null = null;
  role: string | null = null;
  isLoggedIn = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.authService.isLoggedIn().subscribe(isAuth => {
      this.isLoggedIn = isAuth;

      if (isAuth) {
        // Optional: Replace with your own logic or token parsing
        this.username = this.authService.getUsername();
        this.authService.getUserRole().subscribe(role => {
          this.role = role;
        });
      } else {
        this.username = 'Guest';
        this.role = null;
      }
    });
  }

  login() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.authService.logout();
  }
}
