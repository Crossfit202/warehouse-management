package com.jonathans.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "warehouse_storage_locations")
public class WarehouseStorageLocations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "storage_location_id", nullable = false)
    private StorageLocation storageLocation;

    public WarehouseStorageLocations() {}

    public WarehouseStorageLocations(Warehouse warehouse, StorageLocation storageLocation) {
        this.warehouse = warehouse;
        this.storageLocation = storageLocation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

}
