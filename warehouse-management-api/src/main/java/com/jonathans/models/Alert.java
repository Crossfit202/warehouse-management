package com.jonathans.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column
    private String message;

    @Column
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime time = LocalDateTime.now();

    public Alert() {
    }

    public Alert(Warehouse warehouse, String message, String status, User assignedUser) {
        this.warehouse = warehouse;
        this.message = message;
        this.status = status;
        this.time = LocalDateTime.now();
        this.assignedUser = assignedUser;
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

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Alert [id=" + id + ", warehouse=" + warehouse + ", assignedUser=" + assignedUser + ", message="
                + message + ", status=" + status
                + ", time=" + time + "]";
    }

}
