package com.jonathans.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "storage_locations")
public class StorageLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int max_capacity;

    @OneToMany(mappedBy = "storageLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryItem> inventoryItems;

    public StorageLocation() {
    }

    public StorageLocation(String name, int max_capacity) {
        this.name = name;
        this.max_capacity = max_capacity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    @Override
    public String toString() {
        return "StorageLocation [id=" + id + ", name=" + name + ", max_capacity=" + max_capacity + "]";
    }
}
