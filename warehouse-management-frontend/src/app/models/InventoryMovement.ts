export interface InventoryMovement {
    id: string;
    itemName: string;
    itemId?: string; // optional if you want linking/searching
    fromWarehouseName?: string;
    toWarehouseName?: string;
    fromWarehouseId?: string;
    toWarehouseId?: string;
    quantity: number;
    movementType: string;
    userName: string;
    userId?: string;
    time: string;
}
