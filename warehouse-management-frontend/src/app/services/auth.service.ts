import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, of, tap, catchError, map } from 'rxjs';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authStatus = new BehaviorSubject<boolean>(false); // Tracks live auth status
  authStatus$ = this.authStatus.asObservable();


  private baseUrl = 'http://localhost:8083/auth';
  private isAuthenticated: boolean = false;

  constructor(private http: HttpClient, private router: Router) { }

  // ✅ Login
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, { username, password }, { withCredentials: true })
      .pipe(
        tap(response => {
          console.log('Login successful:', response);
          this.authStatus.next(true);
          localStorage.setItem('username', username);
          if (response?.userId) {
            localStorage.setItem('userId', response.userId); // ✅ Save userId from backend response
          } else if (response?.id) {
            localStorage.setItem('userId', response.id); // ✅ Save userId from backend response
          }
          // After successful login
          if (response?.role) {
            localStorage.setItem('role', response.role);
          } else {
            this.http.get<any>(`${this.baseUrl}/verify`, { withCredentials: true }).subscribe(verifyResp => {
              if (verifyResp?.role) {
                localStorage.setItem('role', verifyResp.role);
              }
            });
          }
        }),
        catchError(error => {
          console.error('Login failed:', error);
          this.authStatus.next(false);
          return of(null);
        })
      );
  }



  // ✅ Logout
  logout(): void {
    this.http.post(`${this.baseUrl}/logout`, {}, {
      withCredentials: true,
      responseType: 'text' as 'json'
    }).subscribe({
      next: () => {
        console.log('Logout successful');
        this.isAuthenticated = false;
        localStorage.clear(); // Clear all user info
        this.authStatus.next(false);
        window.location.href = '/'; // Reload app to root
      },
      error: (error) => {
        console.error('Logout failed:', error);
        localStorage.clear();
        this.authStatus.next(false);
        window.location.href = '/';
      }
    });
  }



  // ✅ Check if Logged In
  isLoggedIn(): Observable<boolean> {
    if (this.isAuthenticated) {
      return of(true); // Return immediately if already authenticated
    }

    return this.verifyToken().pipe(
      tap(isValid => {
        this.isAuthenticated = isValid;
        if (!isValid) {
          console.warn('Token invalid or not found');
        }
      })
    );
  }

  // ✅ Get User Role
  getUserRole(): Observable<string | null> {
    return this.http.get<any>(`${this.baseUrl}/verify`, { withCredentials: true })
      .pipe(
        map(response => {
          if (response?.role) {
            console.log('User role:', response.role);
            return response.role;
          }
          console.warn('Role not found in response');
          return null;
        }),
        catchError(error => {
          console.error('Failed to retrieve user role:', error);
          return of(null);
        })
      );
  }

  // Get a username for dashboard message
  getUsername(): string | null {
    return localStorage.getItem('username');
  }


  // ✅ Verify Token
  verifyToken(): Observable<boolean> {
    return this.http.get<any>(`${this.baseUrl}/verify`, { withCredentials: true }).pipe(
      map(response => {
        console.log('Token verified successfully');
        this.authStatus.next(true);  // ✅ Valid token
        return true;
      }),
      catchError(error => {
        console.warn('Token verification failed:', error);
        this.authStatus.next(false); // ✅ Invalid token
        return of(false);
      })
    );
  }

  getCurrentUser(): any {
    const id = localStorage.getItem('userId');
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role'); // <-- Add this
    return id ? { id, username, role } : null;
  }

}
