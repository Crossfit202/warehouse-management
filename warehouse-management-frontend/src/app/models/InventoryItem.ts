export class InventoryItem {
    id: string;
    itemId: string; // ✅ NEW: For move operation
    warehouseId: string; // ✅ NEW
    storageLocationId: string; // ✅ NEW

    sku: string;
    name: string;
    description: string;
    storageLocationName: string;
    created_at: string;
    quantity?: number;

    constructor(
        id: string,
        sku: string,
        name: string,
        description: string,
        storageLocationName: string,
        created_at: string,
        quantity?: number,
        itemId?: string,
        warehouseId?: string,
        storageLocationId?: string
    ) {
        this.id = id;
        this.itemId = itemId ?? id; // fallback if needed
        this.warehouseId = warehouseId ?? '';
        this.storageLocationId = storageLocationId ?? '';

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
