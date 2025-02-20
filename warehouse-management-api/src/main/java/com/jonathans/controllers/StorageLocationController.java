package com.jonathans.controllers;

import com.jonathans.DTOS.StorageLocationDTO;
import com.jonathans.services.StorageLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/storage-locations")
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    public StorageLocationController(StorageLocationService storageLocationService) {
        this.storageLocationService = storageLocationService;
    }

    // ✅ GET ALL STORAGE LOCATIONS
    @GetMapping
    public List<StorageLocationDTO> getAllStorageLocations() {
        return storageLocationService.getAllStorageLocations();
    }

    // ✅ GET STORAGE LOCATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<StorageLocationDTO> getStorageLocationById(@PathVariable UUID id) {
        return storageLocationService.getStorageLocationById(id);
    }

    // ✅ CREATE A NEW STORAGE LOCATION
    @PostMapping
    public ResponseEntity<StorageLocationDTO> createStorageLocation(@RequestBody StorageLocationDTO storageLocationDTO) {
        return storageLocationService.createStorageLocation(storageLocationDTO);
    }

    // ✅ UPDATE STORAGE LOCATION
    @PutMapping("/{id}")
    public ResponseEntity<StorageLocationDTO> updateStorageLocation(@PathVariable UUID id, @RequestBody StorageLocationDTO storageLocationDTO) {
        return storageLocationService.updateStorageLocation(id, storageLocationDTO);
    }

    // ✅ DELETE STORAGE LOCATION
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageLocation(@PathVariable UUID id) {
        return storageLocationService.deleteStorageLocation(id);
    }
}
