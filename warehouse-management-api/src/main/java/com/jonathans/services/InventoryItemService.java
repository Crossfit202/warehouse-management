package com.jonathans.services;

import com.jonathans.DTOS.InventoryItemDTO;
import com.jonathans.models.InventoryItem;
import com.jonathans.models.Warehouse;
import com.jonathans.models.WarehouseInventory;
import com.jonathans.models.WarehouseStorageLocations;
import com.jonathans.repositories.InventoryItemRepository;
import com.jonathans.repositories.StorageLocationRepository;
import com.jonathans.repositories.WarehouseInventoryRepository;
import com.jonathans.repositories.WarehouseStorageLocationsRepository;
import com.jonathans.repositories.WarehouseRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseStorageLocationsRepository warehouseStorageLocationsRepository;
    private final WarehouseRepository warehouseRepository;

    public InventoryItemService(
            InventoryItemRepository inventoryItemRepository,
            StorageLocationRepository storageLocationRepository,
            WarehouseInventoryRepository warehouseInventoryRepository,
            WarehouseStorageLocationsRepository warehouseStorageLocationsRepository,
            WarehouseRepository warehouseRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.warehouseInventoryRepository = warehouseInventoryRepository;
        this.warehouseStorageLocationsRepository = warehouseStorageLocationsRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<InventoryItemDTO> getAllItems() {
        return inventoryItemRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<InventoryItemDTO> getItemById(UUID id) {
        return inventoryItemRepository.findById(id)
                .map(item -> ResponseEntity.ok(convertToDTO(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<InventoryItemDTO> createItem(InventoryItemDTO itemDTO) {
        if (inventoryItemRepository.existsBySku(itemDTO.getSku())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        WarehouseStorageLocations warehouseStorageLocation = null;
        if (itemDTO.getStorageLocationId() != null) {
            Optional<WarehouseStorageLocations> locationOpt = warehouseStorageLocationsRepository
                    .findById(itemDTO.getStorageLocationId());
            if (locationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            warehouseStorageLocation = locationOpt.get();
        }

        InventoryItem item = new InventoryItem(
                itemDTO.getSku(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                warehouseStorageLocation);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(item));
    }

    @Transactional
    public ResponseEntity<InventoryItemDTO> updateItem(UUID id, InventoryItemDTO itemDTO) {
        return inventoryItemRepository.findById(id).map(existingItem -> {
            existingItem.setSku(itemDTO.getSku());
            existingItem.setName(itemDTO.getName());
            existingItem.setDescription(itemDTO.getDescription());

            if (itemDTO.getStorageLocationId() != null) {
                Optional<WarehouseStorageLocations> locationOpt = warehouseStorageLocationsRepository
                        .findById(itemDTO.getStorageLocationId());
                locationOpt.ifPresent(existingItem::setWarehouseStorageLocation);

            }

            inventoryItemRepository.save(existingItem);
            return ResponseEntity.ok(convertToDTO(existingItem));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Object> deleteItem(UUID id) {
        return inventoryItemRepository.findById(id).map(existingItem -> {
            inventoryItemRepository.delete(existingItem);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private InventoryItemDTO convertToDTO(InventoryItem item) {
        return new InventoryItemDTO(
                item.getId(),
                item.getSku(),
                item.getName(),
                item.getDescription(),
                item.getWarehouseStorageLocation() != null ? item.getWarehouseStorageLocation().getId() : null,
                null // quantity is unknown in this context
        );
    }

    public List<InventoryItemDTO> getItemsByWarehouseId(UUID warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));

        List<WarehouseInventory> entries = warehouseInventoryRepository
                .findByWarehouseStorageLocation_Warehouse(warehouse);

        return entries.stream()
                .map(entry -> new InventoryItemDTO(
                        entry.getItem().getId(),
                        entry.getItem().getSku(),
                        entry.getItem().getName(),
                        entry.getItem().getDescription(),
                        entry.getItem().getWarehouseStorageLocation() != null
                                ? entry.getItem().getWarehouseStorageLocation().getId()
                                : null,
                        entry.getQuantity()))
                .collect(Collectors.toList());
    }
}
