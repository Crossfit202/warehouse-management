package com.jonathans.services;

import com.jonathans.DTOS.WarehouseInventoryDTO;
import com.jonathans.DTOS.MoveInventoryDTO;
import com.jonathans.models.*;
import com.jonathans.repositories.WarehouseInventoryRepository;
import com.jonathans.repositories.InventoryMovementRepository;
import com.jonathans.repositories.WarehouseStorageLocationsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class WarehouseInventoryService {

    private final WarehouseInventoryRepository inventoryRepository;
    private final InventoryMovementRepository movementRepository;
    private final WarehouseStorageLocationsRepository storageLocationsRepository;

    public WarehouseInventoryService(
            WarehouseInventoryRepository inventoryRepository,
            InventoryMovementRepository movementRepository,
            WarehouseStorageLocationsRepository storageLocationsRepository // <-- add this
    ) {
        this.inventoryRepository = inventoryRepository;
        this.movementRepository = movementRepository;
        this.storageLocationsRepository = storageLocationsRepository; // <-- add this
    }

    public List<WarehouseInventoryDTO> getInventoryByWarehouse(UUID warehouseId) {
        return inventoryRepository.findByWarehouseStorageLocation_Warehouse_Id(warehouseId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public WarehouseInventoryDTO addInventory(WarehouseInventoryDTO dto) {
        WarehouseInventory entity = new WarehouseInventory();
        entity.setItem(buildInventoryItem(dto.getItemId()));
        entity.setWarehouseStorageLocation(buildStorageLocation(dto.getStorageLocationId()));
        entity.setQuantity(dto.getQuantity());
        entity.setMinQuantity(dto.getMinQuantity());
        entity = inventoryRepository.save(entity);

        // --- LOG MOVEMENT ---
        InventoryMovement movement = new InventoryMovement();
        movement.setItem(entity.getItem());
        movement.setToWarehouse(entity.getWarehouseStorageLocation().getWarehouse());
        movement.setToLocation(entity.getWarehouseStorageLocation());
        movement.setQuantity(dto.getQuantity());
        movement.setMovementType("ADD");
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            movement.setUser(user);
        }
        movement.setTime(LocalDateTime.now());
        movementRepository.save(movement);

        return toDTO(entity);
    }

    public WarehouseInventoryDTO editInventory(WarehouseInventoryDTO dto) {
        WarehouseInventory entity = inventoryRepository.findById(dto.getWarehouseInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        int oldQuantity = entity.getQuantity();
        entity.setQuantity(dto.getQuantity());
        entity.setWarehouseStorageLocation(buildStorageLocation(dto.getStorageLocationId()));
        entity.setMinQuantity(dto.getMinQuantity());
        entity = inventoryRepository.save(entity);

        // --- LOG MOVEMENT if quantity changed ---
        if (dto.getQuantity() < oldQuantity) {
            InventoryMovement movement = new InventoryMovement();
            movement.setItem(entity.getItem());
            movement.setFromWarehouse(entity.getWarehouseStorageLocation().getWarehouse());
            movement.setFromLocation(entity.getWarehouseStorageLocation());
            movement.setQuantity(oldQuantity - dto.getQuantity());
            movement.setMovementType("REDUCE");
            if (dto.getUserId() != null) {
                User user = new User();
                user.setId(dto.getUserId());
                movement.setUser(user);
            }
            movement.setTime(LocalDateTime.now());
            movementRepository.save(movement);
        } else if (dto.getQuantity() > oldQuantity) {
            InventoryMovement movement = new InventoryMovement();
            movement.setItem(entity.getItem());
            movement.setToWarehouse(entity.getWarehouseStorageLocation().getWarehouse());
            movement.setToLocation(entity.getWarehouseStorageLocation());
            movement.setQuantity(dto.getQuantity() - oldQuantity);
            movement.setMovementType("ADD");
            if (dto.getUserId() != null) {
                User user = new User();
                user.setId(dto.getUserId());
                movement.setUser(user);
            }
            movement.setTime(LocalDateTime.now());
            movementRepository.save(movement);
        }

        return toDTO(entity);
    }

    public void deleteInventory(UUID id, UUID userId) {
        WarehouseInventory entity = inventoryRepository.findById(id).orElse(null);
        if (entity != null) {
            InventoryMovement movement = new InventoryMovement();
            movement.setItem(entity.getItem());
            movement.setFromWarehouse(entity.getWarehouseStorageLocation().getWarehouse());
            movement.setFromLocation(entity.getWarehouseStorageLocation());
            movement.setQuantity(entity.getQuantity());
            movement.setMovementType("DELETE");
            if (userId != null) {
                User user = new User();
                user.setId(userId);
                movement.setUser(user);
            }
            movement.setTime(LocalDateTime.now());
            movementRepository.save(movement);

            inventoryRepository.deleteById(id);
        }
    }

    @Transactional
    public void moveInventory(MoveInventoryDTO dto) {
        if (dto.getFromWarehouseId() == null || dto.getToWarehouseId() == null) {
            throw new IllegalArgumentException("Both source and destination warehouse IDs must be provided.");
        }

        WarehouseInventory source = inventoryRepository
                .findByWarehouseStorageLocation_Warehouse_IdAndItem_IdAndWarehouseStorageLocation_Id(
                        dto.getFromWarehouseId(), dto.getItemId(), dto.getFromLocationId())
                .orElseThrow(() -> new RuntimeException("Source inventory record not found"));

        if (source.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough quantity to move");
        }

        source.setQuantity(source.getQuantity() - dto.getQuantity());
        // DELETE source if quantity is now zero, otherwise save
        if (source.getQuantity() <= 0) {
            inventoryRepository.delete(source);
        } else {
            inventoryRepository.save(source);
        }

        WarehouseInventory dest = inventoryRepository
                .findByWarehouseStorageLocation_Warehouse_IdAndItem_IdAndWarehouseStorageLocation_Id(
                        dto.getToWarehouseId(), dto.getItemId(), dto.getToLocationId())
                .orElseGet(() -> {
                    WarehouseInventory newRecord = new WarehouseInventory();
                    newRecord.setItem(buildInventoryItem(dto.getItemId()));
                    newRecord.setWarehouseStorageLocation(buildStorageLocation(dto.getToLocationId()));
                    newRecord.setQuantity(0);
                    newRecord.setMinQuantity(0);
                    return newRecord;
                });

        dest.setQuantity(dest.getQuantity() + dto.getQuantity());
        inventoryRepository.save(dest);

        InventoryMovement movement = new InventoryMovement();
        movement.setItem(buildInventoryItem(dto.getItemId()));
        movement.setFromWarehouse(buildWarehouse(dto.getFromWarehouseId()));
        movement.setToWarehouse(buildWarehouse(dto.getToWarehouseId()));
        movement.setQuantity(dto.getQuantity());
        movement.setMovementType("TRANSFER");
        movement.setFromLocation(buildStorageLocation(dto.getFromLocationId())); // <-- Add this if supported
        movement.setToLocation(buildStorageLocation(dto.getToLocationId())); // <-- Add this if supported
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            movement.setUser(user);
        }
        movement.setTime(LocalDateTime.now());
        movementRepository.save(movement);
    }

    private WarehouseInventoryDTO toDTO(WarehouseInventory entity) {
        WarehouseInventoryDTO dto = new WarehouseInventoryDTO();
        dto.setWarehouseInventoryId(entity.getWarehouseInventoryId());
        dto.setWarehouseId(entity.getWarehouseStorageLocation().getWarehouse().getId());
        dto.setWarehouseName(entity.getWarehouseStorageLocation().getWarehouse().getName()); // ✅ NEW
        dto.setItemId(entity.getItem().getId());
        dto.setItemName(entity.getItem().getName());
        dto.setStorageLocationId(entity.getWarehouseStorageLocation().getId());
        dto.setStorageLocationName(entity.getWarehouseStorageLocation().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setMinQuantity(entity.getMinQuantity());
        dto.setItemSku(entity.getItem().getSku());
        dto.setItemDescription(entity.getItem().getDescription());
        return dto;
    }

    private InventoryItem buildInventoryItem(UUID itemId) {
        InventoryItem item = new InventoryItem();
        item.setId(itemId);
        return item;
    }

    private WarehouseStorageLocations buildStorageLocation(UUID locationId) {
        return storageLocationsRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Storage location not found: " + locationId));
    }

    private Warehouse buildWarehouse(UUID warehouseId) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseId);
        return warehouse;
    }
}
