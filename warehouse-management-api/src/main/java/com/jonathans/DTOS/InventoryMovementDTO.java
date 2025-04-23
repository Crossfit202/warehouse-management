package com.jonathans.DTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public class InventoryMovementDTO {
    private UUID itemId;
    private UUID fromWarehouseId;
    private UUID toWarehouseId;
    private int quantity;
    private String movementType;
    private UUID userId;
    private LocalDateTime time;

    public InventoryMovementDTO() {
    }

    public InventoryMovementDTO(UUID itemId, UUID fromWarehouseId, UUID toWarehouseId, int quantity,
            String movementType, UUID userId, LocalDateTime time) {
        this.itemId = itemId;
        this.fromWarehouseId = fromWarehouseId;
        this.toWarehouseId = toWarehouseId;
        this.quantity = quantity;
        this.movementType = movementType;
        this.userId = userId;
        this.time = time;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "InventoryMovementDTO [itemId=" + itemId + ", fromWarehouseId=" + fromWarehouseId + ", toWarehouseId="
                + toWarehouseId + ", quantity=" + quantity + ", movementType=" + movementType + ", userId=" + userId
                + ", time=" + time + ", getItemId()=" + getItemId() + ", getFromWarehouseId()=" + getFromWarehouseId()
                + ", getToWarehouseId()=" + getToWarehouseId() + ", getQuantity()=" + getQuantity()
                + ", getMovementType()=" + getMovementType() + ", getClass()=" + getClass() + ", getUserId()="
                + getUserId() + ", getTime()=" + getTime() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }

}
