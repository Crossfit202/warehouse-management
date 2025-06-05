package com.jonathans.services;

import com.jonathans.DTOS.InventoryItemDTO;
import com.jonathans.models.InventoryItem;
import com.jonathans.models.WarehouseInventory;
import com.jonathans.repositories.InventoryItemRepository;
import com.jonathans.repositories.StorageLocationRepository;
import com.jonathans.repositories.WarehouseInventoryRepository;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    public InventoryItemService(
            InventoryItemRepository inventoryItemRepository,
            StorageLocationRepository storageLocationRepository,
            WarehouseInventoryRepository warehouseInventoryRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.warehouseInventoryRepository = warehouseInventoryRepository;
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

        InventoryItem item = new InventoryItem();
        item.setSku(itemDTO.getSku());
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());

        inventoryItemRepository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(item));
    }

    @Transactional
    public ResponseEntity<InventoryItemDTO> updateItem(UUID id, InventoryItemDTO itemDTO) {
        return inventoryItemRepository.findById(id).map(existingItem -> {
            existingItem.setSku(itemDTO.getSku());
            existingItem.setName(itemDTO.getName());
            existingItem.setDescription(itemDTO.getDescription());

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
                null, // no shelf
                null // no quantity
        );
    }

    public List<InventoryItemDTO> getItemsByWarehouse(UUID warehouseId) {
        List<WarehouseInventory> inventoryList = warehouseInventoryRepository
                .findByWarehouseStorageLocation_Warehouse_Id(warehouseId); // ✅ Use new method

        return inventoryList.stream().map(wi -> {
            InventoryItem item = wi.getItem();
            String shelfName = wi.getWarehouseStorageLocation().getName(); // ✅ Show shelf

            return new InventoryItemDTO(
                    item.getId(),
                    item.getSku(),
                    item.getName(),
                    item.getDescription(),
                    shelfName,
                    wi.getQuantity());
        }).collect(Collectors.toList());
    }

}
