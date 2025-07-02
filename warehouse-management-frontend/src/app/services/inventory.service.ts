import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WarehouseInventory } from '../models/WarehouseInventory';
import { MoveInventoryDTO } from '../models/MoveInventoryDTO'; // ✅ Use imported version only
import { AddInventoryDTO } from '../models/AddInventoryDTO';

@Injectable({
    providedIn: 'root'
})
export class InventoryService {
    private apiUrl = 'http://localhost:8083/warehouse-inventory';

    constructor(private http: HttpClient) { }

    getInventoryForWarehouse(warehouseId: string): Observable<WarehouseInventory[]> {
        return this.http.get<WarehouseInventory[]>(`${this.apiUrl}/by-warehouse/${warehouseId}`, { withCredentials: true });
    }

    moveInventory(dto: MoveInventoryDTO): Observable<any> {
        return this.http.post(`${this.apiUrl}/move`, dto, { withCredentials: true });
    }

    addInventoryItem(dto: AddInventoryDTO) {
        return this.http.post(`${this.apiUrl}/add`, dto, { withCredentials: true });
    }

    editInventoryItem(dto: WarehouseInventory) {
        return this.http.put(`${this.apiUrl}/edit`, dto, { withCredentials: true });
    }

    deleteInventoryItem(itemId: string, userId: string) {
        return this.http.delete(`${this.apiUrl}/${itemId}?userId=${userId}`, { withCredentials: true });
    }

    getAllInventory() {
        return this.http.get<WarehouseInventory[]>(`${this.apiUrl}/all`, { withCredentials: true });
    }
}
