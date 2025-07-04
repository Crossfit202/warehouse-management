package com.jonathans.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.jonathans.models.InventoryItem;
import com.jonathans.models.Warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonathans.models.WarehouseInventory;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, UUID> {

    List<WarehouseInventory> findByWarehouseStorageLocation_Warehouse(Warehouse warehouse);

    List<WarehouseInventory> findByWarehouseStorageLocation_Warehouse_Id(UUID warehouseId); // ✅ added

    List<WarehouseInventory> findAllByItem(InventoryItem item);

    List<WarehouseInventory> findByWarehouseStorageLocationId(UUID warehouseStorageLocationId);

    Optional<WarehouseInventory> findByWarehouseStorageLocation_Warehouse_IdAndItem_IdAndWarehouseStorageLocation_Id(
            UUID warehouseId, UUID itemId, UUID warehouseStorageLocationId);

    Optional<WarehouseInventory> findByItem_IdAndWarehouseStorageLocation_Id(UUID itemId, UUID storageLocationId);

    boolean existsByItem_Id(UUID itemId);

    boolean existsByWarehouseStorageLocation_Id(UUID warehouseStorageLocationId);
}
