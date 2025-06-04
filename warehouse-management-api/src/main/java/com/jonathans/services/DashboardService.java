// package com.jonathans.services;

// import org.springframework.stereotype.Service;

// import com.jonathans.DTOS.DashboardDTO;
// import com.jonathans.repositories.AlertRepository;
// import com.jonathans.repositories.InventoryItemRepository;
// import com.jonathans.repositories.WarehouseRepository;

// @Service
// public class DashboardService {

// private final InventoryItemRepository inventoryItemRepo;
// private final WarehouseRepository warehouseRepo;
// private final AlertRepository alertsRepo;
// private final InventoryMovementRepository movementRepo;

// public DashboardService(InventoryItemRepository inventoryItemRepo,
// WarehouseRepository warehouseRepo,
// AlertRepository alertsRepo,
// InventoryMovementRepository movementRepo) {
// this.inventoryItemRepo = inventoryItemRepo;
// this.warehouseRepo = warehouseRepo;
// this.alertsRepo = alertsRepo;
// this.movementRepo = movementRepo;
// }

// public DashboardDTO getDashboardData() {
// long totalItems = inventoryItemRepo.count();
// long activeWarehouses = warehouseRepo.count();
// long openAlerts = alertsRepo.countByStatus("NEW");
// long recentMovements = movementRepo.countToday();

// return new DashboardDTO(totalItems, activeWarehouses, openAlerts,
// recentMovements);
// }
// }
