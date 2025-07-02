package com.jonathans.DTOS;

import java.util.UUID;

public class WarehouseInventoryDTO {
    private UUID warehouseInventoryId;
    private UUID warehouseId;
    private String warehouseName; // ✅ NEW
    private UUID itemId;
    private String itemName;
    private UUID storageLocationId;
    private String storageLocationName;
    private int quantity;
    private int minQuantity;
    private String itemSku;
    private String itemDescription; // ✅ NEW
    private UUID userId; // <-- Add this field

    // Constructors
    public WarehouseInventoryDTO() {
    }

    public WarehouseInventoryDTO(UUID warehouseInventoryId, UUID warehouseId, String warehouseName, UUID itemId,
            String itemName, UUID storageLocationId, String storageLocationName, int quantity, int minQuantity,
            String itemSku, String itemDescription, UUID userId) {
        this.warehouseInventoryId = warehouseInventoryId;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.storageLocationId = storageLocationId;
        this.storageLocationName = storageLocationName;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.itemSku = itemSku;
        this.itemDescription = itemDescription; // ✅ NEW
        this.userId = userId; // <-- Add this field
    }

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

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public UUID getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(UUID storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
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

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public UUID getUserId() { // <-- Add getter
        return userId;
    }

    public void setUserId(UUID userId) { // <-- Add setter
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "WarehouseInventoryDTO [warehouseInventoryId=" + warehouseInventoryId + ", warehouseId=" + warehouseId
                + ", warehouseName=" + warehouseName + ", itemId=" + itemId + ", itemName=" + itemName
                + ", storageLocationId=" + storageLocationId + ", storageLocationName=" + storageLocationName
                + ", quantity=" + quantity + ", minQuantity=" + minQuantity + ", itemSku=" + itemSku
                + ", itemDescription=" + itemDescription + ", userId=" + userId // <-- Add this field
                + ", getWarehouseInventoryId()=" + getWarehouseInventoryId()
                + ", getClass()=" + getClass() + ", getWarehouseId()=" + getWarehouseId() + ", getWarehouseName()="
                + getWarehouseName() + ", getItemId()=" + getItemId() + ", getItemName()=" + getItemName()
                + ", getStorageLocationId()=" + getStorageLocationId() + ", getStorageLocationName()="
                + getStorageLocationName() + ", getQuantity()=" + getQuantity() + ", getMinQuantity()="
                + getMinQuantity() + ", getItemSku()=" + getItemSku() + ", getItemDescription()=" + getItemDescription()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
