package com.jonathans.repositories;

import com.jonathans.models.WarehousePersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WarehousePersonnelRepository extends JpaRepository<WarehousePersonnel, UUID> {
    List<WarehousePersonnel> findByWarehouseId(UUID warehouseId);
}
