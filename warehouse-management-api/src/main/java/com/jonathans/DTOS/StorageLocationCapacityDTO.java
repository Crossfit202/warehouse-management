package com.jonathans.DTOS;

import java.util.UUID;

public class StorageLocationCapacityDTO {
    private UUID id;
    private String name;
    private int currentCapacity;
    private int maxCapacity;

    public StorageLocationCapacityDTO() {
    }

    public StorageLocationCapacityDTO(UUID id, String name, int currentCapacity, int maxCapacity) {
        this.id = id;
        this.name = name;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
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

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return "StorageLocationCapacityDTO [id=" + id + ", name=" + name + ", currentCapacity=" + currentCapacity
                + ", maxCapacity=" + maxCapacity + "]";
    }

}
