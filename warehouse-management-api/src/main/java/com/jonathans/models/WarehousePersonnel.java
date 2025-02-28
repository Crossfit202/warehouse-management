package com.jonathans.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "warehouse_personnel")
public class WarehousePersonnel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonnelStatusEnum status = PersonnelStatusEnum.ACTIVE;

    public WarehousePersonnel() {}

    public WarehousePersonnel(User user, Warehouse warehouse, PersonnelStatusEnum status) {
        this.user = user;
        this.warehouse = warehouse;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public PersonnelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PersonnelStatusEnum status) {
        this.status = status;
    }
}
