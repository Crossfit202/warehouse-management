package com.jonathans.controllers;

import com.jonathans.DTOS.WarehouseInventoryDTO;
import com.jonathans.DTOS.MoveInventoryDTO;
import com.jonathans.services.WarehouseInventoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/warehouse-inventory")
public class WarehouseInventoryController {

    private final WarehouseInventoryService warehouseInventoryService;

    public WarehouseInventoryController(WarehouseInventoryService warehouseInventoryService) {
        this.warehouseInventoryService = warehouseInventoryService;
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    public List<WarehouseInventoryDTO> getInventoryByWarehouse(@PathVariable UUID warehouseId) {
        return warehouseInventoryService.getInventoryByWarehouse(warehouseId);
    }

    @PostMapping("/add")
    public WarehouseInventoryDTO addInventory(@RequestBody WarehouseInventoryDTO dto) {
        return warehouseInventoryService.addInventory(dto);
    }

    @PutMapping("/edit")
    public WarehouseInventoryDTO peditInventory(@RequestBody WarehouseInventoryDTO dto) {
        return warehouseInventoryService.editInventory(dto);
    }

    // DELETE INVENTORY
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable UUID id, @RequestParam(required = false) UUID userId) {
        warehouseInventoryService.deleteInventory(id, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/move")
    public ResponseEntity<?> moveInventory(@RequestBody MoveInventoryDTO dto) {
        warehouseInventoryService.moveInventory(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<WarehouseInventoryDTO> getAllInventory() {
        return warehouseInventoryService.getAllInventory();
    }
}
