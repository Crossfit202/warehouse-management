export interface WarehouseInventory {
    warehouseInventoryId: string;
    warehouseId: string;
    warehouseName?: string;
    itemId: string;
    itemName: string;
    itemSku?: string;           // <-- Add this
    itemDescription?: string;   // <-- Add this
    storageLocationId: string;
    storageLocationName: string;
    quantity: number;
    minQuantity: number;
}
