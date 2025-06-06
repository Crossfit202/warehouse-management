package com.jonathans.DTOS;

import java.util.UUID;

public class MoveInventoryDTO {
    private UUID fromWarehouseId;
    private UUID toWarehouseId;
    private UUID itemId;
    private UUID fromLocationId;
    private UUID toLocationId;
    private int quantity;
    private UUID userId; // ✅ Track who made the move
    private String itemName; // ✅ optional display
    private String fromWarehouseName; // ✅ optional display
    private String toWarehouseName; // ✅ optional display

    public MoveInventoryDTO() {
    }

    public MoveInventoryDTO(UUID fromWarehouseId, UUID toWarehouseId, UUID itemId, UUID fromLocationId,
            UUID toLocationId, int quantity, UUID userId, String itemName,
            String fromWarehouseName, String toWarehouseName) {
        this.fromWarehouseId = fromWarehouseId;
        this.toWarehouseId = toWarehouseId;
        this.itemId = itemId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.quantity = quantity;
        this.userId = userId;
        this.itemName = itemName;
        this.fromWarehouseName = fromWarehouseName;
        this.toWarehouseName = toWarehouseName;
    }

    public UUID getFromWarehouseId() {
        return fromWarehouseId;
    }

    public void setFromWarehouseId(UUID fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }

    public UUID getToWarehouseId() {
        return toWarehouseId;
    }

    public void setToWarehouseId(UUID toWarehouseId) {
        this.toWarehouseId = toWarehouseId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(UUID fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public UUID getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(UUID toLocationId) {
        this.toLocationId = toLocationId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getFromWarehouseName() {
        return fromWarehouseName;
    }

    public void setFromWarehouseName(String fromWarehouseName) {
        this.fromWarehouseName = fromWarehouseName;
    }

    public String getToWarehouseName() {
        return toWarehouseName;
    }

    public void setToWarehouseName(String toWarehouseName) {
        this.toWarehouseName = toWarehouseName;
    }

    @Override
    public String toString() {
        return "MoveInventoryDTO [fromWarehouseId=" + fromWarehouseId + ", toWarehouseId=" + toWarehouseId +
                ", itemId=" + itemId + ", fromLocationId=" + fromLocationId + ", toLocationId=" + toLocationId +
                ", quantity=" + quantity + ", userId=" + userId + ", itemName=" + itemName +
                ", fromWarehouseName=" + fromWarehouseName + ", toWarehouseName=" + toWarehouseName + "]";
    }
}