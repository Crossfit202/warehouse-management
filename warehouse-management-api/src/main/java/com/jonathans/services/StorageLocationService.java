package com.jonathans.services;

import com.jonathans.DTOS.StorageLocationDTO;
import com.jonathans.models.StorageLocation;
import com.jonathans.repositories.StorageLocationRepository;
import com.jonathans.repositories.WarehouseInventoryRepository;
import com.jonathans.repositories.WarehouseStorageLocationsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageLocationService {

    private final StorageLocationRepository storageLocationRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseStorageLocationsRepository warehouseStorageLocationsRepository;

    public StorageLocationService(
            StorageLocationRepository storageLocationRepository,
            WarehouseInventoryRepository warehouseInventoryRepository,
            WarehouseStorageLocationsRepository warehouseStorageLocationsRepository // <-- add this
    ) {
        this.storageLocationRepository = storageLocationRepository;
        this.warehouseInventoryRepository = warehouseInventoryRepository;
        this.warehouseStorageLocationsRepository = warehouseStorageLocationsRepository; // <-- assign it
    }

    // ✅ GET ALL STORAGE LOCATIONS
    public List<StorageLocationDTO> getAllStorageLocations() {
        return storageLocationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET STORAGE LOCATION BY ID
    public ResponseEntity<StorageLocationDTO> getStorageLocationById(UUID id) {
        return storageLocationRepository.findById(id)
                .map(location -> ResponseEntity.ok(convertToDTO(location)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // ✅ CREATE STORAGE LOCATION
    @Transactional
    public ResponseEntity<StorageLocationDTO> createStorageLocation(StorageLocationDTO storageLocationDTO) {
        if (storageLocationDTO.getName() == null || storageLocationDTO.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (storageLocationRepository.existsByName(storageLocationDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        StorageLocation storageLocation = new StorageLocation(
                storageLocationDTO.getName(),
                storageLocationDTO.getMax_capacity());

        storageLocation = storageLocationRepository.save(storageLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(storageLocation));
    }

    // ✅ UPDATE STORAGE LOCATION
    @Transactional
    public ResponseEntity<StorageLocationDTO> updateStorageLocation(UUID id, StorageLocationDTO storageLocationDTO) {
        return storageLocationRepository.findById(id).map(existingLocation -> {
            if (storageLocationDTO.getName() != null) {
                existingLocation.setName(storageLocationDTO.getName());
            }
            if (storageLocationDTO.getMax_capacity() > 0) {
                existingLocation.setMax_capacity(storageLocationDTO.getMax_capacity());
            }

            StorageLocation updatedLocation = storageLocationRepository.save(existingLocation);
            return ResponseEntity.ok(convertToDTO(updatedLocation));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // ✅ DELETE STORAGE LOCATION
    @Transactional
    public ResponseEntity<Void> deleteStorageLocation(UUID id) {
        if (!storageLocationRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        storageLocationRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ✅ Convert Entity to DTO
    private StorageLocationDTO convertToDTO(StorageLocation storageLocation) {
        return new StorageLocationDTO(storageLocation.getId(), storageLocation.getName(),
                storageLocation.getMax_capacity());
    }

    public boolean isLocationReferenced(UUID id) {
        return warehouseInventoryRepository.existsByWarehouseStorageLocation_Id(id);
    }

    @Transactional
    public void removeStorageLocationFromWarehouse(UUID warehouseId, UUID warehouseStorageLocationId) {
        // If you have a WarehouseStorageLocationsRepository, use it to delete the association.
        // Example:
        warehouseStorageLocationsRepository.deleteById(warehouseStorageLocationId);
    }
}
