package com.jonathans.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "warehouse_inventory")
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID warehouseInventoryId;

    // 🔄 NEW: Each inventory record is linked to a warehouse storage location
    @ManyToOne
    @JoinColumn(name = "warehouse_storage_location_id", nullable = false)
    private WarehouseStorageLocations warehouseStorageLocation;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    private int quantity;

    public WarehouseInventory() {
    }

    public WarehouseInventory(UUID warehouseInventoryId, WarehouseStorageLocations warehouseStorageLocation,
            InventoryItem item, int quantity) {
        this.warehouseInventoryId = warehouseInventoryId;
        this.warehouseStorageLocation = warehouseStorageLocation;
        this.item = item;
        this.quantity = quantity;
    }

    public UUID getWarehouseInventoryId() {
        return warehouseInventoryId;
    }

    public void setWarehouseInventoryId(UUID warehouseInventoryId) {
        this.warehouseInventoryId = warehouseInventoryId;
    }

    public WarehouseStorageLocations getWarehouseStorageLocation() {
        return warehouseStorageLocation;
    }

    public void setWarehouseStorageLocation(WarehouseStorageLocations warehouseStorageLocation) {
        this.warehouseStorageLocation = warehouseStorageLocation;
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "WarehouseInventory{" +
                "warehouseInventoryId=" + warehouseInventoryId +
                ", warehouseStorageLocation="
                + (warehouseStorageLocation != null ? warehouseStorageLocation.getId() : null) +
                ", item=" + (item != null ? item.getId() : null) +
                ", quantity=" + quantity +
                '}';
    }
}
