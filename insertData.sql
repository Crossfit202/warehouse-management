-- ✅ Insert Users with a Single Role
-- INSERT INTO users (id, username, email, password, role) VALUES 
--     (uuid_generate_v4(), 'Jonathan', 'admin@example.com', 'securepassword', 'ROLE_ADMIN'),
--     (uuid_generate_v4(), 'Maxwell', 'manager@example.com', 'securepassword', 'ROLE_MANAGER'),

-- ✅ Insert Warehouses
INSERT INTO warehouse (id, name, location, max_capacity) VALUES 
    (uuid_generate_v4(), 'Main Warehouse', 'New York', 10000),
    (uuid_generate_v4(), 'Backup Warehouse', 'Los Angeles', 5000);

-- ✅ Insert Generic Storage Location Templates
INSERT INTO storage_locations (id, name, max_capacity) VALUES 
    (uuid_generate_v4(), 'Large Shelf', 500),
    (uuid_generate_v4(), 'Medium Shelf', 300);

-- ✅ Insert Warehouse-Specific Shelf Instances (warehouse_storage_locations now includes a unique name and links back to storage template)
INSERT INTO warehouse_storage_locations (id, warehouse_id, storage_location_template_id, name, max_capacity)
SELECT uuid_generate_v4(), w.id, s.id, 
    CASE 
        WHEN w.name = 'Main Warehouse' AND s.name = 'Large Shelf' THEN 'Main-LS1'
        WHEN w.name = 'Main Warehouse' AND s.name = 'Medium Shelf' THEN 'Main-MS1'
        WHEN w.name = 'Backup Warehouse' AND s.name = 'Large Shelf' THEN 'Backup-LS1'
        WHEN w.name = 'Backup Warehouse' AND s.name = 'Medium Shelf' THEN 'Backup-MS1'
    END,
    s.max_capacity
FROM warehouse w, storage_locations s;

-- ✅ Insert Inventory Items (NO shelf reference anymore)
INSERT INTO inventory_items (id, sku, name, description)
VALUES
    (uuid_generate_v4(), 'ITEM1001', 'Laptop', 'Dell XPS 15 Laptop'),
    (uuid_generate_v4(), 'ITEM2002', 'Smartphone', 'iPhone 13 Pro');

-- ✅ Add Inventory Quantities to Warehouse Inventory table WITH STORAGE LOCATION
INSERT INTO warehouse_inventory (warehouse_inventory_id, warehouse_id, warehouse_storage_location_id, item_id, quantity)
SELECT 
    uuid_generate_v4(), 
    w.id, 
    wsl.id,
    i.id, 
    CASE WHEN i.name = 'Laptop' THEN 100 ELSE 150 END
FROM warehouse w
JOIN inventory_items i ON 
    (i.sku = 'ITEM1001' AND w.name = 'Main Warehouse')
    OR (i.sku = 'ITEM2002' AND w.name = 'Backup Warehouse')
JOIN warehouse_storage_locations wsl ON 
    (wsl.name = 'Main-LS1' AND w.name = 'Main Warehouse')
    OR (wsl.name = 'Backup-MS1' AND w.name = 'Backup Warehouse');

-- ✅ Assign Personnel to Warehouses
INSERT INTO warehouse_personnel (id, user_id, warehouse_id, status)
SELECT uuid_generate_v4(), u.id, w.id, 'ACTIVE'
FROM users u, warehouse w
WHERE (u.username = 'manager_user' AND w.name = 'Main Warehouse')
   OR (u.username = 'clerk_user' AND w.name = 'Backup Warehouse');

-- ✅ Create Inventory Movement Logs
INSERT INTO inventory_movement (id, item_id, from_warehouse, to_warehouse, quantity, movement_type, user_id)
SELECT uuid_generate_v4(), i.id, NULL, w.id, 50, 'ADD', u.id
FROM warehouse w, inventory_items i, users u
WHERE w.name = 'Main Warehouse'
  AND i.sku = 'ITEM1001'
  AND u.username = 'admin_user';

-- ✅ Insert Alerts
INSERT INTO alerts (id, warehouse_id, message, status)
SELECT uuid_generate_v4(), w.id, 'Warehouse nearing capacity!', 'NEW'
FROM warehouse w
WHERE w.name = 'Main Warehouse';
