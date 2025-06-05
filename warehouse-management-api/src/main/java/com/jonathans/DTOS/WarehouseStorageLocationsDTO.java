package com.jonathans.DTOS;

import java.util.UUID;

public class WarehouseStorageLocationsDTO {

    private UUID id;
    private UUID warehouseId;
    private String warehouseName;

    private UUID storageLocationTemplateId; // renamed
    private String templateName; // renamed

    private String name; // new: instance label (e.g., "Shelf A1")
    private int maxCapacity; // new
    private int currentCapacity; // new

    public WarehouseStorageLocationsDTO() {
    }

    public WarehouseStorageLocationsDTO(UUID id, UUID warehouseId, String warehouseName,
            UUID storageLocationTemplateId, String templateName,
            String name, int maxCapacity, int currentCapacity) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.storageLocationTemplateId = storageLocationTemplateId;
        this.templateName = templateName;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
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

    public UUID getStorageLocationTemplateId() {
        return storageLocationTemplateId;
    }

    public void setStorageLocationTemplateId(UUID storageLocationTemplateId) {
        this.storageLocationTemplateId = storageLocationTemplateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
}
