package com.jonathans.services;

import com.jonathans.DTOS.StorageLocationDTO;
import com.jonathans.DTOS.WarehouseStorageLocationsDTO;
import com.jonathans.models.StorageLocation;
import com.jonathans.models.Warehouse;
import com.jonathans.models.WarehouseStorageLocations;
import com.jonathans.repositories.WarehouseStorageLocationsRepository;
import com.jonathans.repositories.StorageLocationRepository;
import com.jonathans.repositories.WarehouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseStorageLocationService {

    private final WarehouseStorageLocationsRepository warehouseStorageLocationsRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final WarehouseRepository warehouseRepository;

    public WarehouseStorageLocationService(WarehouseStorageLocationsRepository warehouseStorageLocationsRepository,
            StorageLocationRepository storageLocationRepository, WarehouseRepository warehouseRepository) {
        this.warehouseStorageLocationsRepository = warehouseStorageLocationsRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<WarehouseStorageLocationsDTO> getAllWarehouseStorageLocations() {
        return warehouseStorageLocationsRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<WarehouseStorageLocationsDTO> getWarehouseStorageLocationsById(UUID id) {
        return warehouseStorageLocationsRepository.findById(id)
                .map(location -> ResponseEntity.ok(convertToDTO(location)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<WarehouseStorageLocationsDTO> createWarehouseStorageLocation(
            WarehouseStorageLocationsDTO dto) {
        Optional<StorageLocation> storageLocationOpt = storageLocationRepository.findById(dto.getStorageLocationId());
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(dto.getWarehouseId());

        if (storageLocationOpt.isEmpty() || warehouseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        WarehouseStorageLocations locations = new WarehouseStorageLocations(warehouseOpt.get(),
                storageLocationOpt.get());
        locations = warehouseStorageLocationsRepository.save(locations);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(locations));
    }

    private WarehouseStorageLocationsDTO convertToDTO(WarehouseStorageLocations locations) {
        return new WarehouseStorageLocationsDTO(
                locations.getId(),
                locations.getStorageLocation().getId(),
                locations.getStorageLocation().getName(), // Include storagelocation name
                locations.getWarehouse().getId(),
                locations.getWarehouse().getName() // Include warehouse name
        );
    }

    public List<StorageLocationDTO> getStorageLocationsByWarehouseId(UUID warehouseId) {
        return warehouseStorageLocationsRepository.findByWarehouseId(warehouseId)
                .stream()
                .map(link -> {
                    StorageLocation s = link.getStorageLocation();
                    return new StorageLocationDTO(s.getId(), s.getName(), s.getMax_capacity());
                })
                .toList();
    }

}
