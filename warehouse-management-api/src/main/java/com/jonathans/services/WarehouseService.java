package com.jonathans.services;

import com.jonathans.DTOS.WarehouseDTO;
import com.jonathans.models.Warehouse;
import com.jonathans.repositories.WarehouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    // ✅ GET ALL WAREHOUSES
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET WAREHOUSE BY ID
    public ResponseEntity<WarehouseDTO> getWarehouseById(UUID warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .map(warehouse -> ResponseEntity.ok(convertToDTO(warehouse)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // ✅ CREATE WAREHOUSE
    @Transactional
    public ResponseEntity<WarehouseDTO> createWarehouse(WarehouseDTO warehouseDTO) {
        // ✅ Create new warehouse entity
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseDTO.getName());
        warehouse.setLocation(warehouseDTO.getLocation());
        warehouse.setMax_capacity(warehouseDTO.getMax_capacity());

        // ✅ Save the warehouse in the database
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        // ✅ Convert saved warehouse to DTO and return
        WarehouseDTO responseDTO = new WarehouseDTO(
            savedWarehouse.getId(),
            savedWarehouse.getName(),
            savedWarehouse.getLocation(),
            savedWarehouse.getMax_capacity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    // ✅ UPDATE WAREHOUSE
    @Transactional
    public ResponseEntity<WarehouseDTO> updateWarehouse(UUID warehouseId, WarehouseDTO warehouseDTO) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
        if (warehouseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Warehouse existingWarehouse = warehouseOptional.get();

        if (warehouseDTO.getName() != null) existingWarehouse.setName(warehouseDTO.getName());
        if (warehouseDTO.getLocation() != null) existingWarehouse.setLocation(warehouseDTO.getLocation());
        if (warehouseDTO.getMax_capacity() > 0) existingWarehouse.setMax_capacity(warehouseDTO.getMax_capacity());

        warehouseRepository.save(existingWarehouse);
        return ResponseEntity.ok(convertToDTO(existingWarehouse));
    }

    // ✅ DELETE WAREHOUSE
    @Transactional
    public ResponseEntity<Void> deleteWarehouse(UUID warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        warehouseRepository.deleteById(warehouseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ✅ Convert Warehouse to WarehouseDTO
    private WarehouseDTO convertToDTO(Warehouse warehouse) {
        return new WarehouseDTO(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation(),
                warehouse.getMax_capacity()
        );
    }
}
