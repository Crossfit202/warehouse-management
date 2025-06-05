package com.jonathans.DTOS;

import java.util.UUID;

public class InventoryItemDTO {

    private UUID id;
    private String sku;
    private String name;
    private String description;
    private String storageLocationName;
    private Integer quantity;

    public InventoryItemDTO() {
    }

    public InventoryItemDTO(UUID id, String sku, String name, String description, String storageLocationName,
            Integer quantity) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.storageLocationName = storageLocationName;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "InventoryItemDTO [id=" + id + ", sku=" + sku + ", name=" + name + ", description=" + description
                + ", storageLocationName=" + storageLocationName + ", quantity=" + quantity + "]";
    }

}
