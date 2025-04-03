import { StorageLocationsComponent } from "../components/storage-locations/storage-locations.component";
import { StorageLocation } from "./StorageLocation";

export class InventoryItem {
    id: string; // is UUID in DB 
    sku: string;
    name: string;
    description: string;
    storageLocation: StorageLocation
    created_at: string; //LocalDateTime in DB 

    constructor(
        id: string,
        sku: string,
        name: string,
        description: string,
        storageLocation: StorageLocation,
        created_at: string,
    ) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.storageLocation = storageLocation;
        this.created_at = created_at;
    }

    //Formatting the date
    public formatCreatedAt(): string {
        if (!this.created_at) return 'N/A';

        let cleaned = this.created_at.replace(' ', 'T');
        cleaned = cleaned.split('.')[0]; // remove microseconds

        const date = new Date(cleaned);
        if (isNaN(date.getTime())) {
            console.warn('Invalid date:', this.created_at);
            return 'Invalid Date';
        }

        return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    }



}