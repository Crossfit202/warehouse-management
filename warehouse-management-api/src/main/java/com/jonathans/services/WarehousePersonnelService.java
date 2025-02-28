package com.jonathans.services;

import com.jonathans.DTOS.WarehousePersonnelDTO;
import com.jonathans.models.User;
import com.jonathans.models.Warehouse;
import com.jonathans.models.WarehousePersonnel;
import com.jonathans.repositories.UserRepository;
import com.jonathans.repositories.WarehousePersonnelRepository;
import com.jonathans.repositories.WarehouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehousePersonnelService {

    private final WarehousePersonnelRepository warehousePersonnelRepository;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;

    public WarehousePersonnelService(WarehousePersonnelRepository warehousePersonnelRepository, UserRepository userRepository, WarehouseRepository warehouseRepository) {
        this.warehousePersonnelRepository = warehousePersonnelRepository;
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<WarehousePersonnelDTO> getAllWarehousePersonnel() {
        return warehousePersonnelRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<WarehousePersonnelDTO> getWarehousePersonnelById(UUID id) {
        return warehousePersonnelRepository.findById(id)
                .map(personnel -> ResponseEntity.ok(convertToDTO(personnel)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<WarehousePersonnelDTO> createWarehousePersonnel(WarehousePersonnelDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(dto.getWarehouseId());

        if (userOpt.isEmpty() || warehouseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        WarehousePersonnel personnel = new WarehousePersonnel(userOpt.get(), warehouseOpt.get(), dto.getStatus());
        personnel = warehousePersonnelRepository.save(personnel);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(personnel));
    }

    private WarehousePersonnelDTO convertToDTO(WarehousePersonnel personnel) {
        return new WarehousePersonnelDTO(
                personnel.getId(),
                personnel.getUser().getId(),
                personnel.getUser().getUsername(), // Include username
                personnel.getWarehouse().getId(),
                personnel.getWarehouse().getName(), // Include warehouse name
                personnel.getStatus()
        );
    }
}
