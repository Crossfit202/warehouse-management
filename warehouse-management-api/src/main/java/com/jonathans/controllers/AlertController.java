package com.jonathans.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathans.DTOS.AlertDTO;
import com.jonathans.services.AlertService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<AlertDTO> createAlert(AlertDTO alertDTO) {
        return alertService.createAlert(alertDTO);
    }

    // UDPATE ALERT
    @PostMapping("/{id}")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable UUID id, @RequestBody AlertDTO alertDTO) {
        return alertService.updateAlert(id, alertDTO);
    }

    // DELETE ALERT BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable UUID id) {
        return alertService.deleteAlert(id);
    }

}
