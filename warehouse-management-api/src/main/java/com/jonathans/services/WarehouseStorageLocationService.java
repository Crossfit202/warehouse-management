package com.jonathans.services;

import com.jonathans.DTOS.StorageLocationDTO;
import com.jonathans.DTOS.StorageLocationCapacityDTO;
import com.jonathans.DTOS.WarehouseStorageLocationsDTO;
import com.jonathans.models.InventoryItem;
import com.jonathans.models.StorageLocation;
import com.jonathans.models.Warehouse;
import com.jonathans.models.WarehouseInventory;
import com.jonathans.models.WarehouseStorageLocations;
import com.jonathans.repositories.InventoryItemRepository;
import com.jonathans.repositories.StorageLocationRepository;
import com.jonathans.repositories.WarehouseInventoryRepository;
import com.jonathans.repositories.WarehouseRepository;
import com.jonathans.repositories.WarehouseStorageLocationsRepository;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseStorageLocationService {

    private final WarehouseStorageLocationsRepository warehouseStorageLocationsRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    public WarehouseStorageLocationService(
            WarehouseStorageLocationsRepository warehouseStorageLocationsRepository,
            StorageLocationRepository storageLocationRepository,
            WarehouseRepository warehouseRepository,
            InventoryItemRepository inventoryItemRepository,
            WarehouseInventoryRepository warehouseInventoryRepository) {
        this.warehouseStorageLocationsRepository = warehouseStorageLocationsRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.warehouseInventoryRepository = warehouseInventoryRepository;
    }

    public List<WarehouseStorageLocationsDTO> getAllWarehouseStorageLocations() {
        return warehouseStorageLocationsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
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
                locations.getStorageLocation().getName(),
                locations.getWarehouse().getId(),
                locations.getWarehouse().getName());
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

    public List<StorageLocationCapacityDTO> getStorageLocationCapacities(UUID warehouseId) {
        List<WarehouseStorageLocations> links = warehouseStorageLocationsRepository.findByWarehouseId(warehouseId);

        return links.stream().map(link -> {
            StorageLocation s = link.getStorageLocation();

            // Get all inventory items assigned to this storage location
            List<InventoryItem> itemsInLocation = inventoryItemRepository.findByStorageLocation(s);

            // Sum quantities from warehouse_inventory table
            int usedCapacity = itemsInLocation.stream()
                    .flatMap(item -> warehouseInventoryRepository.findAllByItem(item).stream())
                    .mapToInt(WarehouseInventory::getQuantity)
                    .sum();

            return new StorageLocationCapacityDTO(
                    s.getId(),
                    s.getName(),
                    usedCapacity,
                    s.getMax_capacity());
        }).toList();
    }
}
