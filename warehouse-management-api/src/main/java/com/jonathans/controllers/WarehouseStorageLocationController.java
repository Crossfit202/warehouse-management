package com.jonathans.controllers;

import com.jonathans.DTOS.WarehouseStorageLocationsDTO;
import com.jonathans.services.WarehouseStorageLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/warehouse-storage-locations")
public class WarehouseStorageLocationController {

    private final WarehouseStorageLocationService warehouseStorageLocationService;

    public WarehouseStorageLocationController(WarehouseStorageLocationService warehouseStorageLocationService) {
        this.warehouseStorageLocationService = warehouseStorageLocationService;
    }

    @GetMapping
    public List<WarehouseStorageLocationsDTO> getAllWarehouseStorageLocations() {
        return warehouseStorageLocationService.getAllWarehouseStorageLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseStorageLocationsDTO> getWarehouseStorageLocationsById(@PathVariable UUID id) {
        return warehouseStorageLocationService.getWarehouseStorageLocationsById(id);
    }

    @PostMapping
    public ResponseEntity<WarehouseStorageLocationsDTO> createWarehouseStorageLocation(
            @RequestBody WarehouseStorageLocationsDTO dto) {
        return warehouseStorageLocationService.createWarehouseStorageLocation(dto);
    }

    @DeleteMapping("/{warehouseId}/storage-locations/{warehouseStorageLocationId}")
    public ResponseEntity<Void> removeStorageLocationFromWarehouse(
            @PathVariable UUID warehouseId,
            @PathVariable UUID warehouseStorageLocationId) {
        warehouseStorageLocationService.removeStorageLocationFromWarehouse(warehouseId, warehouseStorageLocationId);
        return ResponseEntity.noContent().build();
    }
}
