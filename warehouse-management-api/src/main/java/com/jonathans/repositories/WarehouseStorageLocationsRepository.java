package com.jonathans.repositories;

import com.jonathans.models.WarehouseStorageLocations;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarehouseStorageLocationsRepository extends JpaRepository<WarehouseStorageLocations, UUID> {

    @Query("SELECT wsl FROM WarehouseStorageLocations wsl WHERE wsl.warehouse.id = :warehouseId")
    List<WarehouseStorageLocations> findByWarehouseId(@Param("warehouseId") UUID warehouseId);
}
