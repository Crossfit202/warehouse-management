package com.jonathans.controllers;

import com.jonathans.DTOS.WarehousePersonnelDTO;
import com.jonathans.services.WarehousePersonnelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/warehouse-personnel")
public class WarehousePersonnelController {

    private final WarehousePersonnelService warehousePersonnelService;

    public WarehousePersonnelController(WarehousePersonnelService warehousePersonnelService) {
        this.warehousePersonnelService = warehousePersonnelService;
    }

    @GetMapping
    public List<WarehousePersonnelDTO> getAllWarehousePersonnel() {
        return warehousePersonnelService.getAllWarehousePersonnel();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehousePersonnelDTO> getWarehousePersonnelById(@PathVariable UUID id) {
        return warehousePersonnelService.getWarehousePersonnelById(id);
    }

    @PostMapping
    public ResponseEntity<WarehousePersonnelDTO> createWarehousePersonnel(@RequestBody WarehousePersonnelDTO dto) {
        return warehousePersonnelService.createWarehousePersonnel(dto);
    }

    @GetMapping("/warehouse/{warehouseId}/users")
    public List<WarehousePersonnelDTO> getUsersForWarehouse(@PathVariable UUID warehouseId) {
        return warehousePersonnelService.getUsersForWarehouse(warehouseId);
    }

    @GetMapping("/user/{userId}/warehouses")
    public List<WarehousePersonnelDTO> getWarehousesForUser(@PathVariable UUID userId) {
        return warehousePersonnelService.getWarehousesForUser(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehousePersonnelDTO> updateWarehousePersonnel(
            @PathVariable UUID id,
            @RequestBody WarehousePersonnelDTO dto) {
        return warehousePersonnelService.updateWarehousePersonnel(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehousePersonnel(@PathVariable UUID id) {
        warehousePersonnelService.deleteWarehousePersonnel(id);
        return ResponseEntity.noContent().build();
    }
}
