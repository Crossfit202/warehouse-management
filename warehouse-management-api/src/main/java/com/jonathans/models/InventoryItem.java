package com.jonathans.models;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 🔄 Changed to match new schema: point to WarehouseStorageLocation, not
    // StorageLocation
    @ManyToOne
    @JoinColumn(name = "warehouse_storage_location_id", referencedColumnName = "id", nullable = true)
    private WarehouseStorageLocations warehouseStorageLocation;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public InventoryItem() {
    }

    public InventoryItem(String sku, String name, String description,
            WarehouseStorageLocations warehouseStorageLocation) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.warehouseStorageLocation = warehouseStorageLocation;
        this.createdAt = LocalDateTime.now();
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

    public WarehouseStorageLocations getWarehouseStorageLocation() {
        return warehouseStorageLocation;
    }

    public void setWarehouseStorageLocation(WarehouseStorageLocations warehouseStorageLocation) {
        this.warehouseStorageLocation = warehouseStorageLocation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "InventoryItem [id=" + id + ", sku=" + sku + ", name=" + name +
                ", description=" + description +
                ", warehouseStorageLocation="
                + (warehouseStorageLocation != null ? warehouseStorageLocation.getId() : null) +
                ", createdAt=" + createdAt + "]";
    }
}
