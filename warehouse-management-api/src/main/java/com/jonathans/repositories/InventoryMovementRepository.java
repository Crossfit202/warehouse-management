package com.jonathans.repositories;

import com.jonathans.models.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, UUID> {

    @Query("SELECT m FROM InventoryMovement m " +
            "WHERE m.fromWarehouse.id = :warehouseId OR m.toWarehouse.id = :warehouseId " +
            "ORDER BY m.time DESC")
    List<InventoryMovement> findByWarehouseId(UUID warehouseId);

    @Query("SELECT m FROM InventoryMovement m ORDER BY m.time DESC")
    List<InventoryMovement> findAllMovementsOrdered();
}
