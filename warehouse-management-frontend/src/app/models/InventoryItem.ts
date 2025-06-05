import { StorageLocationsComponent } from "../components/storage-locations/storage-locations.component";
import { StorageLocation } from "./StorageLocation";

export class InventoryItem {
    id: string;
    sku: string;
    name: string;
    description: string;
    storageLocationName: string; // ✅ was: storageLocation: StorageLocation
    created_at: string;
    quantity?: number;

    constructor(
        id: string,
        sku: string,
        name: string,
        description: string,
        storageLocationName: string, // ✅ was: StorageLocation
        created_at: string,
        quantity?: number
    ) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.storageLocationName = storageLocationName;
        this.created_at = created_at;
        this.quantity = quantity;
    }

    public formatCreatedAt(): string {
        if (!this.created_at) return 'N/A';
        let cleaned = this.created_at.replace(' ', 'T');
        cleaned = cleaned.split('.')[0];
        const date = new Date(cleaned);
        return isNaN(date.getTime()) ? 'Invalid Date' : `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    }
}
