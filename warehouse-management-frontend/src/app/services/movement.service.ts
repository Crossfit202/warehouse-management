import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InventoryMovement } from '../models/InventoryMovement';

@Injectable({
    providedIn: 'root'
})
export class MovementService {
    private apiUrl = 'http://localhost:8083/movements';

    constructor(private http: HttpClient) { }

    getMovementsForWarehouse(warehouseId: string): Observable<InventoryMovement[]> {
        return this.http.get<InventoryMovement[]>(`${this.apiUrl}/by-warehouse/${warehouseId}`, { withCredentials: true });
    }

    getAllMovements(): Observable<InventoryMovement[]> {
        return this.http.get<InventoryMovement[]>(`${this.apiUrl}/all`, { withCredentials: true });
    }
}
