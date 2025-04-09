import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Warehouse } from '../models/Warehouse';
import { Alert } from '../models/Alert';

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
}
