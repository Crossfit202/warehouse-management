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

}
