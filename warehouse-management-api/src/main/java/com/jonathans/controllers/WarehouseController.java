package com.jonathans.controllers;

import com.jonathans.DTOS.WarehouseDTO;
import com.jonathans.services.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // ✅ GET ALL WAREHOUSES
    @GetMapping
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }

    // ✅ GET WAREHOUSE BY ID
    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable UUID warehouseId) {
        return warehouseService.getWarehouseById(warehouseId);
    }

    // ✅ CREATE A NEW WAREHOUSE
    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.createWarehouse(warehouseDTO);
    }

    // ✅ UPDATE WAREHOUSE DETAILS
    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(
            @PathVariable UUID warehouseId,
            @RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.updateWarehouse(warehouseId, warehouseDTO);
    }

    // ✅ DELETE WAREHOUSE BY ID
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable UUID warehouseId) {
        return warehouseService.deleteWarehouse(warehouseId);
    }
}
