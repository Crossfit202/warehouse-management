package com.jonathans.services;

import com.jonathans.DTOS.InventoryItemDTO;
import com.jonathans.models.InventoryItem;
import com.jonathans.models.StorageLocation;
import com.jonathans.repositories.InventoryItemRepository;
import com.jonathans.repositories.StorageLocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final StorageLocationRepository storageLocationRepository;

    public InventoryItemService(InventoryItemRepository inventoryItemRepository, StorageLocationRepository storageLocationRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.storageLocationRepository = storageLocationRepository;
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

        StorageLocation storageLocation = null;
        if (itemDTO.getStorageLocationId() != null) {
            Optional<StorageLocation> locationOpt = storageLocationRepository.findById(itemDTO.getStorageLocationId());
            if (locationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            storageLocation = locationOpt.get();
        }

        InventoryItem item = new InventoryItem(itemDTO.getSku(), itemDTO.getName(), itemDTO.getDescription(), storageLocation);
        item = inventoryItemRepository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(item));
    }

    @Transactional
    public ResponseEntity<InventoryItemDTO> updateItem(UUID id, InventoryItemDTO itemDTO) {
        return inventoryItemRepository.findById(id).map(existingItem -> {
            existingItem.setSku(itemDTO.getSku());
            existingItem.setName(itemDTO.getName());
            existingItem.setDescription(itemDTO.getDescription());

            if (itemDTO.getStorageLocationId() != null) {
                Optional<StorageLocation> locationOpt = storageLocationRepository.findById(itemDTO.getStorageLocationId());
                if (locationOpt.isPresent()) {
                    existingItem.setStorageLocation(locationOpt.get());
                }
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
                item.getStorageLocation() != null ? item.getStorageLocation().getId() : null
        );
    }
}
