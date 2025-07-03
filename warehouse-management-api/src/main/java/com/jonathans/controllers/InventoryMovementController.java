package com.jonathans.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathans.DTOS.InventoryMovementViewDTO;
import com.jonathans.services.InventoryMovementService;

@RestController
@RequestMapping("/movements")
public class InventoryMovementController {

    @Autowired
    private InventoryMovementService movementService;

    @GetMapping("/by-warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryMovementViewDTO>> getMovements(@PathVariable UUID warehouseId) {
        List<InventoryMovementViewDTO> movements = movementService.getMovementsForWarehouse(warehouseId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryMovementViewDTO>> getAllMovements() {
        List<InventoryMovementViewDTO> movements = movementService.getAllMovements();
        return ResponseEntity.ok(movements);
    }
}
