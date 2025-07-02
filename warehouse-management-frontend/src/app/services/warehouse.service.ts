import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Warehouse } from '../models/Warehouse';

@Injectable({
  providedIn: 'root'
})
export class WarehouseService {

  private apiUrl = 'http://localhost:8083/warehouses'

  constructor(private http: HttpClient) { }

  //GET ALL WAREHOUSES
  getAllWarehouses(): Observable<Warehouse[]> {
    return this.http.get<Warehouse[]>(this.apiUrl, { withCredentials: true });
  }

  getStorageLocationsForWarehouse(warehouseId: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8083/warehouses/${warehouseId}/storage-locations`, { withCredentials: true });
  }

  createWarehouse(warehouse: Partial<Warehouse>) {
    return this.http.post<Warehouse>(this.apiUrl, warehouse, { withCredentials: true });
  }

  updateWarehouse(id: string, warehouse: Warehouse) {
    return this.http.put<Warehouse>(`${this.apiUrl}/${id}`, warehouse, { withCredentials: true });
  }

  deleteWarehouse(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
