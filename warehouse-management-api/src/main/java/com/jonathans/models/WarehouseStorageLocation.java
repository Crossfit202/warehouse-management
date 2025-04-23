package com.jonathans.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "warehouse_storage_locations")
public class WarehouseStorageLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "storage_location_id")
    private StorageLocation storageLocation;

    public WarehouseStorageLocation() {
    }

    public WarehouseStorageLocation(UUID id, Warehouse warehouse, StorageLocation storageLocation) {
        this.id = id;
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

    @Override
    public String toString() {
        return "WarehouseStorageLocation [id=" + id + ", warehouse=" + warehouse + ", storageLocation="
                + storageLocation + ", getId()=" + getId() + ", getWarehouse()=" + getWarehouse()
                + ", getStorageLocation()=" + getStorageLocation() + ", getClass()=" + getClass() + ", hashCode()="
                + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
