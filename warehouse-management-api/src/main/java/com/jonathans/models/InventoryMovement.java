package com.jonathans.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_movement")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    @ManyToOne
    @JoinColumn(name = "from_warehouse")
    private Warehouse fromWarehouse;

    @ManyToOne
    @JoinColumn(name = "to_warehouse")
    private Warehouse toWarehouse;

    private int quantity;
    private String movementType;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public InventoryMovement() {
    }

    public InventoryMovement(UUID id, InventoryItem item, Warehouse fromWarehouse, Warehouse toWarehouse, int quantity,
            String movementType, LocalDateTime time, User user) {
        this.id = id;
        this.item = item;
        this.fromWarehouse = fromWarehouse;
        this.toWarehouse = toWarehouse;
        this.quantity = quantity;
        this.movementType = movementType;
        this.time = time;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public Warehouse getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(Warehouse fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public Warehouse getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(Warehouse toWarehouse) {
        this.toWarehouse = toWarehouse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "InventoryMovement [id=" + id + ", item=" + item + ", fromWarehouse=" + fromWarehouse + ", toWarehouse="
                + toWarehouse + ", quantity=" + quantity + ", movementType=" + movementType + ", time=" + time
                + ", user=" + user + ", getId()=" + getId() + ", getClass()=" + getClass() + ", getItem()=" + getItem()
                + ", getFromWarehouse()=" + getFromWarehouse() + ", getToWarehouse()=" + getToWarehouse()
                + ", getQuantity()=" + getQuantity() + ", getMovementType()=" + getMovementType() + ", getTime()="
                + getTime() + ", getUser()=" + getUser() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }

}
