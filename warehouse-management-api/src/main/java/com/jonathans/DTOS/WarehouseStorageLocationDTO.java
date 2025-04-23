package com.jonathans.DTOS;

import java.util.UUID;

public class WarehouseStorageLocationDTO {
    private UUID id;
    private UUID warehouseId;
    private UUID storageLocationId;

    // Constructors
    public WarehouseStorageLocationDTO() {
    }

    public WarehouseStorageLocationDTO(UUID id, UUID warehouseId, UUID storageLocationId) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.storageLocationId = storageLocationId;
    }

    // Getters & Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UUID getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(UUID storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    @Override
    public String toString() {
        return "WarehouseStorageLocationDTO [id=" + id + ", warehouseId=" + warehouseId + ", storageLocationId="
                + storageLocationId + ", getId()=" + getId() + ", getWarehouseId()=" + getWarehouseId()
                + ", getStorageLocationId()=" + getStorageLocationId() + ", getClass()=" + getClass() + ", hashCode()="
                + hashCode() + ", toString()=" + super.toString() + "]";
    }
}
