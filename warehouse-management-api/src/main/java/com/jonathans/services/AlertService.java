package com.jonathans.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jonathans.DTOS.AlertDTO;
import com.jonathans.models.Alert;
import com.jonathans.repositories.AlertRepository;
import com.jonathans.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    public AlertService(AlertRepository alertRepository, UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
    }

    // GET ALL ALERTS
    public List<AlertDTO> getAllAlerts() {
        return alertRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // GET ALERT BY ID
    public ResponseEntity<AlertDTO> getAlertById(UUID id) {
        return alertRepository.findById(id)
                .map(alert -> ResponseEntity.ok(convertToDTO(alert)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // CREATE ALERT
    @Transactional
    public ResponseEntity<AlertDTO> createAlert(AlertDTO alertDTO) {
        Alert alert = new Alert();
        alert.setMessage(alertDTO.getMessage());
        alert.setStatus(alertDTO.getStatus());
        alert.setWarehouse(alertDTO.getWarehouse());

        // Set assigned user if provided
        if (alertDTO.getAssignedUserId() != null) {
            userRepository.findById(alertDTO.getAssignedUserId()).ifPresent(alert::setAssignedUser);
        }

        Alert savedAlert = alertRepository.save(alert);

        AlertDTO responseAlertDTO = convertToDTO(savedAlert);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseAlertDTO);
    }

    // UPDATE ALERT
    @Transactional
    public ResponseEntity<AlertDTO> updateAlert(UUID id, AlertDTO alertDTO) {
        Optional<Alert> alertOptional = alertRepository.findById(id);
        if (alertOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Alert existingAlert = alertOptional.get();

        if (alertDTO.getMessage() != null)
            existingAlert.setMessage(alertDTO.getMessage());
        if (alertDTO.getStatus() != null)
            existingAlert.setStatus(alertDTO.getStatus());
        if (alertDTO.getWarehouse() != null)
            existingAlert.setWarehouse(alertDTO.getWarehouse());
        if (alertDTO.getAssignedUserId() != null) {
            userRepository.findById(alertDTO.getAssignedUserId()).ifPresent(existingAlert::setAssignedUser);
        } else {
            existingAlert.setAssignedUser(null);
        }

        alertRepository.save(existingAlert);

        return ResponseEntity.ok(convertToDTO(existingAlert));
    }

    // DELETE ALERT
    @Transactional
    public ResponseEntity<Void> deleteAlert(UUID id) {
        if (!alertRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        alertRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Convert to DTO
    private AlertDTO convertToDTO(Alert alert) {
        return new AlertDTO(
                alert.getId(),
                alert.getWarehouse(),
                alert.getMessage(),
                alert.getStatus(),
                alert.getTime(),
                alert.getAssignedUser() != null ? alert.getAssignedUser().getId() : null // <-- Add this
        );
    }
}
