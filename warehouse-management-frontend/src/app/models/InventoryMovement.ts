export interface InventoryMovement {
    id: string;
    itemName: string;
    fromWarehouseName?: string;
    toWarehouseName?: string;
    quantity: number;
    movementType: string;
    userName: string;
    time: string;
}
