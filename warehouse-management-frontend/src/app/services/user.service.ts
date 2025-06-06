import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8083/users'

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, {
      withCredentials: true // ✅ THIS IS CRITICAL FOR AUTHORIZATION - SENDS THE JWT TOKEN TO THE JWTFILTER.JAVA
    });
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(
      `${this.apiUrl}/${user.id}`,
      user,
      { withCredentials: true } // <-- This ensures cookies (JWT) are sent!
    );
  }

  deleteUser(userId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`, {
      withCredentials: true
    });
  }

  getUsersForWarehouse(warehouseId: string): Observable<User[]> {
    return this.http.get<User[]>(`http://localhost:8083/warehouse-personnel/warehouse/${warehouseId}/users`, { withCredentials: true });
  }
}
