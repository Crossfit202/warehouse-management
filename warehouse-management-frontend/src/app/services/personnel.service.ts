import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface WarehousePersonnelDTO {
    id: string;
    userId: string;
    username: string;
    warehouseId: string;
    warehouseName: string;
    status: string;
}

@Injectable({ providedIn: 'root' })
export class WarehousePersonnelService {
    private apiUrl = 'http://localhost:8083/warehouse-personnel';

    constructor(private http: HttpClient) { }

    getPersonnelForWarehouse(warehouseId: string): Observable<WarehousePersonnelDTO[]> {
        return this.http.get<WarehousePersonnelDTO[]>(`${this.apiUrl}/warehouse/${warehouseId}/users`, { withCredentials: true });
    }

    getWarehousesForUser(userId: string): Observable<WarehousePersonnelDTO[]> {
        return this.http.get<WarehousePersonnelDTO[]>(`${this.apiUrl}/user/${userId}/warehouses`, { withCredentials: true });
    }

    updateWarehousePersonnel(id: string, dto: WarehousePersonnelDTO): Observable<WarehousePersonnelDTO> {
        return this.http.put<WarehousePersonnelDTO>(`${this.apiUrl}/${id}`, dto, { withCredentials: true });
    }

    deleteWarehousePersonnel(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
    }

    createWarehousePersonnel(dto: WarehousePersonnelDTO): Observable<WarehousePersonnelDTO> {
        return this.http.post<WarehousePersonnelDTO>(this.apiUrl, dto, { withCredentials: true });
    }
}