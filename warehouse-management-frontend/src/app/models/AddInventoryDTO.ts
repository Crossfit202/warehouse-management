export interface AddInventoryDTO {
    warehouseId: string;
    itemId: string;
    storageLocationId: string;
    quantity: number;
    minQuantity: number;
    userId: string;
}
