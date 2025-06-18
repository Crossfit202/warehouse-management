import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service'; // Adjust path as needed
import { CommonModule } from '@angular/common'; // Import CommonModule for ngIf, ngFor, etc.

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent implements OnInit {
  username: string | null = null;
  role: string | null = null;
  isLoggedIn = false;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    // Example: adjust based on your AuthService API
    this.isLoggedIn = !!localStorage.getItem('userId');
    this.username = localStorage.getItem('username');
    // If you fetch role from backend:
    this.authService.getUserRole().subscribe(role => {
      this.role = role;
    });
  }
}
