package com.jonathans.repositories;

import com.jonathans.models.InventoryItem;
import com.jonathans.models.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {
    boolean existsBySku(String sku);

    List<InventoryItem> findByStorageLocation(StorageLocation location);

}
