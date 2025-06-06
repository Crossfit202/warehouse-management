export interface MoveInventoryDTO {
    fromWarehouseId: string;
    toWarehouseId: string;
    itemId: string;
    fromLocationId: string;
    toLocationId: string;
    quantity: number;
    userId: string; // ✅ Add this line
}
