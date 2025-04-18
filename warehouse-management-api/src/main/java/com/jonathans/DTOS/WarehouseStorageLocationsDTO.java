package com.jonathans.DTOS;

import java.util.UUID;

public class WarehouseStorageLocationsDTO {

    private UUID id;
    private UUID warehouseId;
    private String warehouseName;
    private UUID storageLocationId;
    private String storageName;


    public WarehouseStorageLocationsDTO() {}

    public WarehouseStorageLocationsDTO(UUID id, UUID warehouseId, String warehouseName, UUID storageLocationId, String storageName) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.storageLocationId = storageLocationId;
        this.storageName = storageName;
    }

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

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public UUID getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(UUID storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

}
