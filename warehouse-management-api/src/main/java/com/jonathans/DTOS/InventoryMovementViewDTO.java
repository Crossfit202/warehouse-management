package com.jonathans.DTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public class InventoryMovementViewDTO {
    private UUID id;
    private String itemName;
    private String fromWarehouseName;
    private String toWarehouseName;
    private int quantity;
    private String movementType;
    private String userName;
    private LocalDateTime time;

    public InventoryMovementViewDTO(UUID id, String itemName, String fromWarehouseName, String toWarehouseName,
            int quantity, String movementType, String userName, LocalDateTime time) {
        this.id = id;
        this.itemName = itemName;
        this.fromWarehouseName = fromWarehouseName;
        this.toWarehouseName = toWarehouseName;
        this.quantity = quantity;
        this.movementType = movementType;
        this.userName = userName;
        this.time = time;
    }

    public InventoryMovementViewDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
