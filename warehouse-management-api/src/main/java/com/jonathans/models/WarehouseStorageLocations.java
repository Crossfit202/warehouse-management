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
    @JoinColumn(name = "storage_location_template_id", nullable = false)
    private StorageLocation storageLocationTemplate;

    @Column(name = "name", nullable = false)
    private String name; // Renamed from 'label' to match DB

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "current_capacity")
    private int currentCapacity = 0;

    public WarehouseStorageLocations() {
    }

    public WarehouseStorageLocations(Warehouse warehouse, StorageLocation storageLocationTemplate, String name,
            int maxCapacity) {
        this.warehouse = warehouse;
        this.storageLocationTemplate = storageLocationTemplate;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

    public WarehouseStorageLocations(Warehouse warehouse, StorageLocation storageLocation, String name, int maxCapacity,
            int currentCapacity) {
        this.warehouse = warehouse;
        this.storageLocationTemplate = storageLocation;
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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public StorageLocation getStorageLocationTemplate() {
        return storageLocationTemplate;
    }

    public void setStorageLocationTemplate(StorageLocation storageLocationTemplate) {
        this.storageLocationTemplate = storageLocationTemplate;
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
