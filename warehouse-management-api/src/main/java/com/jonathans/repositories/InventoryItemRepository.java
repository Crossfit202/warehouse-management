package com.jonathans.repositories;

import com.jonathans.models.InventoryItem;
import com.jonathans.models.WarehouseStorageLocations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {
    boolean existsBySku(String sku);

    // ✅ CORRECT — based on your new model
    List<InventoryItem> findByWarehouseStorageLocation(WarehouseStorageLocations warehouseLocation);

}
