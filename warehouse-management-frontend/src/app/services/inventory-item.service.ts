import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class InventoryItemService {
    private apiUrl = 'http://localhost:8083/inventory-items';

    constructor(private http: HttpClient) { }

    getAllItems(): Observable<any[]> {
        return this.http.get<any[]>(this.apiUrl, { withCredentials: true });
    }

    createItem(product: { sku: string; name: string; description: string }) {
        return this.http.post(`${this.apiUrl}/add`, product, {withCredentials: true});
    }

    updateItem(product: { id: string; sku: string; name: string; description: string }) {
        return this.http.put(`${this.apiUrl}/${product.id}`, product, { withCredentials: true });
    }

    deleteItem(id: string) {
        return this.http.delete(`${this.apiUrl}/${id}`, { withCredentials: true });
    }
}