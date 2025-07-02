export interface WarehouseInventory {
    warehouseInventoryId: string;
    warehouseId: string;
    itemId: string;
    itemName?: string;
    itemSku?: string;
    storageLocationId: string;
    storageLocationName?: string;
    quantity: number;
    minQuantity: number;

    // UI-only property for inline editing
    editingMinQuantity?: boolean;
}
