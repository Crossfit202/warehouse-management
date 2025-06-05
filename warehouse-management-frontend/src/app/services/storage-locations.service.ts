import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageLocation } from '../models/StorageLocation';

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



}
