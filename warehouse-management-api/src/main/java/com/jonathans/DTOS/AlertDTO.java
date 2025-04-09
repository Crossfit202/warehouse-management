package com.jonathans.DTOS;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jonathans.models.Warehouse;

public class AlertDTO {
    private UUID id;
    private Warehouse warehouse;
    private String message;
    private String status;
    private LocalDateTime time;

    public AlertDTO() {
    }

    public AlertDTO(UUID id, Warehouse warehouse, String message, String status, LocalDateTime time) {
        this.id = id;
        this.warehouse = warehouse;
        this.message = message;
        this.status = status;
        this.time = time;
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
        return "AlertDTO [id=" + id + ", warehouse=" + warehouse + ", message=" + message + ", status=" + status
                + ", time=" + time + "]";
    }

}
