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

    @Column
    private String message;

    @Column
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime time = LocalDateTime.now();

    public Alert() {
    }

    public Alert(Warehouse warehouse, String message, String status) {
        this.warehouse = warehouse;
        this.message = message;
        this.status = status;
        this.time = LocalDateTime.now();
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
        return "Alert [id=" + id + ", warehouse=" + warehouse + ", message=" + message + ", status=" + status
                + ", time=" + time + "]";
    }

}
