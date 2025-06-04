package com.jonathans.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonathans.models.WarehouseInventory;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, UUID> {
    List<WarehouseInventory> findByWarehouseId(UUID warehouseId);

}
