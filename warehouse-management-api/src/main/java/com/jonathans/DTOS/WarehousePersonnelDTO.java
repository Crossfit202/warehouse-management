package com.jonathans.DTOS;

import java.util.UUID;
import com.jonathans.models.PersonnelStatusEnum;

public class WarehousePersonnelDTO {

    private UUID id;
    private UUID userId;
    private String username;
    private UUID warehouseId;
    private String warehouseName;
    private PersonnelStatusEnum status;

    public WarehousePersonnelDTO() {}

    public WarehousePersonnelDTO(UUID id, UUID userId, String username, UUID warehouseId, String warehouseName, PersonnelStatusEnum status) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public PersonnelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PersonnelStatusEnum status) {
        this.status = status;
    }
}
