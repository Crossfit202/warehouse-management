package com.jonathans.controllers;

import com.jonathans.DTOS.StorageLocationCapacityDTO;
import com.jonathans.DTOS.StorageLocationDTO;
import com.jonathans.DTOS.WarehouseDTO;
import com.jonathans.services.WarehouseService;
import com.jonathans.services.WarehouseStorageLocationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WarehouseStorageLocationService warehouseStorageLocationService;

    public WarehouseController(WarehouseService warehouseService,
            WarehouseStorageLocationService warehouseStorageLocationService) {
        this.warehouseStorageLocationService = warehouseStorageLocationService;
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

    // WarehouseController.java
    @GetMapping("/{id}/storage-locations")
    public List<StorageLocationDTO> getStorageLocationsForWarehouse(@PathVariable UUID id) {
        return warehouseStorageLocationService.getStorageLocationsByWarehouseId(id);
    }

    @GetMapping("/{id}/storage-location-capacities")
    public List<StorageLocationCapacityDTO> getStorageLocationCapacities(@PathVariable UUID id) {
        return warehouseStorageLocationService.getStorageLocationCapacities(id);
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
