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
@Table(name = "warehouse_inventory")
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID warehouseInventoryId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    private int quantity;
    private int minQuantity;

    public WarehouseInventory(UUID warehouseInventoryId, Warehouse warehouse, InventoryItem item, int quantity,
            int minQuantity) {
        this.warehouseInventoryId = warehouseInventoryId;
        this.warehouse = warehouse;
        this.item = item;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
    }

    public WarehouseInventory() {
    }

    public UUID getWarehouseInventoryId() {
        return warehouseInventoryId;
    }

    public void setWarehouseInventoryId(UUID warehouseInventoryId) {
        this.warehouseInventoryId = warehouseInventoryId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
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

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    @Override
    public String toString() {
        return "WarehouseInventory [warehouseInventoryId=" + warehouseInventoryId + ", warehouse=" + warehouse
                + ", item=" + item + ", quantity=" + quantity + ", minQuantity=" + minQuantity
                + ", getWarehouseInventoryId()=" + getWarehouseInventoryId() + ", getWarehouse()=" + getWarehouse()
                + ", getClass()=" + getClass() + ", getItem()=" + getItem() + ", getQuantity()=" + getQuantity()
                + ", getMinQuantity()=" + getMinQuantity() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }

}
