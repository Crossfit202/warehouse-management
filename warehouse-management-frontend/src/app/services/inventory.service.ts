import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InventoryItem } from '../models/InventoryItem';


@Injectable({
    providedIn: 'root'
})
export class InventoryService {

    private apiUrl = 'http://localhost:8083/inventory-items'

    constructor(private http: HttpClient) { }

    //GET ALL WAREHOUSES
    getAllInventory(): Observable<InventoryItem[]> {
        return this.http.get<InventoryItem[]>(this.apiUrl, { withCredentials: true });
    }

    getInventoryForWarehouse(warehouseId: string): Observable<InventoryItem[]> {
        const apiUrl = `${this.apiUrl}/by-warehouse-id?warehouseId=${warehouseId}`;
        return this.http.get<InventoryItem[]>(apiUrl, { withCredentials: true });
    }


}
