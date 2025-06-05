package com.jonathans.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonathans.DTOS.InventoryMovementViewDTO;
import com.jonathans.models.InventoryMovement;
import com.jonathans.repositories.InventoryMovementRepository;

@Service
public class InventoryMovementService {

    @Autowired
    private InventoryMovementRepository movementRepo;

    public List<InventoryMovementViewDTO> getMovementsForWarehouse(UUID warehouseId) {
        List<InventoryMovement> movements = movementRepo.findByWarehouseId(warehouseId);

        return movements.stream().map(m -> new InventoryMovementViewDTO(
                m.getId(),
                m.getItem() != null ? m.getItem().getName() : "N/A",
                m.getFromWarehouse() != null ? m.getFromWarehouse().getName() : "N/A",
                m.getToWarehouse() != null ? m.getToWarehouse().getName() : "N/A",
                m.getQuantity(),
                m.getMovementType(),
                m.getUser() != null ? m.getUser().getUsername() : "N/A",
                m.getTime())).toList();
    }
}
