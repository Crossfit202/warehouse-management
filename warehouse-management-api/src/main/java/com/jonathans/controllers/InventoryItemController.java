package com.jonathans.controllers;

import com.jonathans.DTOS.InventoryItemDTO;
import com.jonathans.services.InventoryItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping
    public List<InventoryItemDTO> getAllItems() {
        return inventoryItemService.getAllItems();
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryItemDTO>> getItemsByWarehouse(@PathVariable UUID warehouseId) {
        List<InventoryItemDTO> items = inventoryItemService.getItemsByWarehouse(warehouseId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getItemById(@PathVariable UUID id) {
        return inventoryItemService.getItemById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryItemDTO> createItem(@RequestBody InventoryItemDTO itemDTO) {
        return inventoryItemService.createItem(itemDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> updateItem(@PathVariable UUID id, @RequestBody InventoryItemDTO itemDTO) {
        return inventoryItemService.updateItem(id, itemDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable UUID id) {
        return inventoryItemService.deleteItem(id);
    }
}
