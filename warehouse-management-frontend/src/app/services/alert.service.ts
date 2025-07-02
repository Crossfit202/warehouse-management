import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Warehouse } from '../models/Warehouse';
import { Alert } from '../models/Alert';
import { WarehousePersonnelDTO } from './personnel.service';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  private apiUrl = 'http://localhost:8083/alerts'

  constructor(private http: HttpClient) { }

  //GET ALL ALERTS
  getAllAlerts(): Observable<Alert[]> {
    return this.http.get<Alert[]>(this.apiUrl, { withCredentials: true });
  }

  updateAlert(alert: any): Observable<Alert> {
    return this.http.post<Alert>(`${this.apiUrl}/${alert.id}`, alert, { withCredentials: true });
  }

  deleteAlert(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  createAlert(alert: any): Observable<Alert> {
    return this.http.post<Alert>(this.apiUrl, alert, { withCredentials: true });
  }

  updateAlertStatus(alertId: string, status: string) {
    return this.http.patch(`${this.apiUrl}/${alertId}/status`, { status }, { withCredentials: true });
  }

  getUsersForWarehouse(warehouseId: string): Observable<WarehousePersonnelDTO[]> {
    return this.http.get<WarehousePersonnelDTO[]>(`http://localhost:8083/warehouse-personnel/warehouse/${warehouseId}/users`, { withCredentials: true });
  }
}
