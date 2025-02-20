package com.jonathans.DTOS;

import java.util.UUID;

public class StorageLocationDTO {
    private UUID id;
    private String name;
    private int max_capacity;

    public StorageLocationDTO() {
    }

    public StorageLocationDTO(UUID id, String name, int max_capacity) {
        this.id = id;
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

    @Override
    public String toString() {
        return "StorageLocationDTO [id=" + id + ", name=" + name + ", max_capacity=" + max_capacity + "]";
    }
}
