import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8083/users';

  constructor(private http: HttpClient) { }

  // Login - Sends credentials and expects a cookie
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, { username, password }, { withCredentials: true });
  }


  // Logout - Invalidates the cookie
  logout(): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/logout`, {}, { withCredentials: true });
  }

  // Example Authenticated API Call
  getUsers(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}`, { withCredentials: true });
  }
}
