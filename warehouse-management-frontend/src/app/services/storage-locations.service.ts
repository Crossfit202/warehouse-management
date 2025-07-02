import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageLocation } from '../models/StorageLocation';
import { StorageLocationCapacity } from '../models/StorageLocationCapacity';

@Injectable({
  providedIn: 'root'
})
export class StorageLocationsService {

  private apiUrl = 'http://localhost:8083/storage-locations'
  private warehouseAPIUrl = 'http://localhost:8083/warehouses'

  constructor(private http: HttpClient) { }

  getAllStorageLocations(): Observable<StorageLocation[]> {
    return this.http.get<StorageLocation[]>(this.apiUrl, { withCredentials: true });
  }

  getStorageLocationsByWarehouse(warehouseId: string): Observable<StorageLocation[]> {
    return this.http.get<StorageLocation[]>(`${this.warehouseAPIUrl}/${warehouseId}/storage-locations`, { withCredentials: true });
  }

  getStorageLocationCapacities(warehouseId: string): Observable<StorageLocationCapacity[]> {
    return this.http.get<StorageLocationCapacity[]>(`http://localhost:8083/warehouses/${warehouseId}/storage-location-capacities`, { withCredentials: true });
  }

  isLocationReferenced(id: string) {
    return this.http.get<boolean>(`${this.apiUrl}/${id}/referenced`, { withCredentials: true });
  }

  createStorageLocation(location: { name: string; max_capacity: number }) {
    return this.http.post(`${this.apiUrl}`, location, { withCredentials: true });
  }

  updateStorageLocation(id: string, location: { name: string; max_capacity: number }) {
    return this.http.put(`${this.apiUrl}/${id}`, location, { withCredentials: true });
  }

  deleteStorageLocation(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
