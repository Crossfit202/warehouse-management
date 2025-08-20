import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/User';
import { WarehousePersonnelDTO } from '../models/WarehousePersonnelDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8083/users'

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(
      `${this.apiUrl}/${user.id}`,
      user,
      { withCredentials: true }
    );
  }

  deleteUser(userId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`, {
      withCredentials: true
    });
  }

  getUsersForWarehouse(warehouseId: string): Observable<WarehousePersonnelDTO[]> {
    return this.http.get<WarehousePersonnelDTO[]>(`http://localhost:8083/warehouse-personnel/warehouse/${warehouseId}/users`, { withCredentials: true });
  }

  createUser(user: { username: string; email: string; password: string; role: string }) {
    return this.http.post(`${this.apiUrl}/register`, user, { withCredentials: true });
  }

  getUserById(id: string) {
    return this.http.get<User>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  getCurrentUser() {
    return this.http.get<any>('http://localhost:8083/users/me', { withCredentials: true });
  }
}
