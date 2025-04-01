import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  register(): void {
    if (this.registerForm.invalid) return;

    this.http.post('http://localhost:8083/users/register', this.registerForm.value, {
      withCredentials: true
    }).subscribe({
      next: () => {
        alert('User registered successfully!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        alert('Registration failed: ' + (err.error?.message || 'Unknown error'));
        console.error(err);
      }
    });
  }
}
