package com.jonathans.DTOS;

import java.util.UUID;

public class InventoryItemDTO {

    private UUID id;
    private String sku;
    private String name;

    private String description;

    private UUID storageLocationId;

    public InventoryItemDTO() {}

    public InventoryItemDTO(UUID id, String sku, String name, String description, UUID storageLocationId) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.storageLocationId = storageLocationId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UUID getStorageLocationId() { return storageLocationId; }
    public void setStorageLocationId(UUID storageLocationId) { this.storageLocationId = storageLocationId; }

    @Override
    public String toString() {
        return "InventoryItemDTO [id=" + id + ", sku=" + sku + ", name=" + name + 
               ", description=" + description + ", storageLocationId=" + storageLocationId + "]";
    }
}
