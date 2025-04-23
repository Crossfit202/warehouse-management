package com.jonathans.DTOS;

import java.util.UUID;

public class WarehouseInventoryDTO {
    private UUID warehouseInventoryId;
    private UUID warehouseId;
    private UUID itemId;
    private int quantity;
    private int minQuantity;

    // Constructors
    public WarehouseInventoryDTO() {
    }

    public WarehouseInventoryDTO(UUID warehouseInventoryId, UUID warehouseId, UUID itemId, int quantity,
            int minQuantity) {
        this.warehouseInventoryId = warehouseInventoryId;
        this.warehouseId = warehouseId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
    }

    // Getters & Setters
    public UUID getWarehouseInventoryId() {
        return warehouseInventoryId;
    }

    public void setWarehouseInventoryId(UUID warehouseInventoryId) {
        this.warehouseInventoryId = warehouseInventoryId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    @Override
    public String toString() {
        return "WarehouseInventoryDTO [warehouseInventoryId=" + warehouseInventoryId + ", warehouseId=" + warehouseId
                + ", itemId=" + itemId + ", quantity=" + quantity + ", minQuantity=" + minQuantity
                + ", getWarehouseInventoryId()=" + getWarehouseInventoryId() + ", getWarehouseId()=" + getWarehouseId()
                + ", getItemId()=" + getItemId() + ", getQuantity()=" + getQuantity() + ", getMinQuantity()="
                + getMinQuantity() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }

}
