package com.jonathans.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathans.DTOS.AlertDTO;
import com.jonathans.services.AlertService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // GET ALL ALERTS
    @GetMapping
    public List<AlertDTO> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    // GET ALERT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> getAlertById(UUID id) {
        return alertService.getAlertById(id);
    }

    // CREATE NEW ALERT
    @PostMapping
    public ResponseEntity<AlertDTO> createAlert(@RequestBody AlertDTO alertDTO) {
        return alertService.createAlert(alertDTO);
    }

    // UPDATE ALERT
    @PostMapping("/{id}")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable UUID id, @RequestBody AlertDTO alertDTO) {
        return alertService.updateAlert(id, alertDTO);
    }

    // DELETE ALERT BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable UUID id) {
        return alertService.deleteAlert(id);
    }

    // UPDATE ALERT STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<AlertDTO> updateAlertStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return alertService.updateAlertStatus(id, status);
    }

}
