-- -- Insert Users with a Single Role
-- INSERT INTO users (id, username, email, password, role) VALUES 
--     (uuid_generate_v4(), 'admin_user', 'admin@example.com', 'securepassword', 'ROLE_ADMIN'),
--     (uuid_generate_v4(), 'manager_user', 'manager@example.com', 'securepassword', 'ROLE_MANAGER'),
--     (uuid_generate_v4(), 'clerk_user', 'clerk@example.com', 'securepassword', 'ROLE_INV_CLERK');

-- Insert Warehouses
INSERT INTO warehouse (id, name, location, max_capacity) VALUES 
    (uuid_generate_v4(), 'Main Warehouse', 'New York', 10000),
    (uuid_generate_v4(), 'Backup Warehouse', 'Los Angeles', 5000);

-- Insert Storage Locations
INSERT INTO storage_locations (id, name, max_capacity) VALUES 
    (uuid_generate_v4(), 'Aisle 1 - Shelf 1', 500),
    (uuid_generate_v4(), 'Aisle 2 - Shelf 3', 300);

-- Map Storage Locations to Warehouses (Many-to-Many)
INSERT INTO warehouse_storage_locations (id, warehouse_id, storage_location_id) 
SELECT uuid_generate_v4(), w.id, s.id 
FROM warehouse w, storage_locations s 
WHERE (w.name = 'Main Warehouse' AND s.name = 'Aisle 1 - Shelf 1') 
   OR (w.name = 'Backup Warehouse' AND s.name = 'Aisle 2 - Shelf 3');

-- Insert Inventory Items
INSERT INTO inventory_items (id, sku, name, storage_location, description) VALUES 
    (uuid_generate_v4(), 'ITEM1001', 'Laptop', NULL, 'Dell XPS 15 Laptop'),
    (uuid_generate_v4(), 'ITEM2002', 'Smartphone', NULL, 'iPhone 13 Pro');

-- Add Inventory Items to Warehouses (Many-to-Many)
INSERT INTO warehouse_inventory (warehouse_inventory_id, warehouse_id, item_id, quantity) 
SELECT uuid_generate_v4(), w.id, i.id, 100 
FROM warehouse w, inventory_items i 
WHERE (w.name = 'Main Warehouse' AND i.sku = 'ITEM1001') 
   OR (w.name = 'Backup Warehouse' AND i.sku = 'ITEM2002');

-- Assign Personnel to Warehouses
INSERT INTO warehouse_personnel (id, user_id, warehouse_id, status) 
SELECT uuid_generate_v4(), u.id, w.id, 'ACTIVE' 
FROM users u, warehouse w 
WHERE (u.username = 'manager_user' AND w.name = 'Main Warehouse') 
   OR (u.username = 'clerk_user' AND w.name = 'Backup Warehouse');

-- Create Inventory Movement Logs
INSERT INTO inventory_movement (id, item_id, from_warehouse, to_warehouse, quantity, movement_type, user_id) 
SELECT uuid_generate_v4(), i.id, NULL, w.id, 50, 'ADD', u.id 
FROM warehouse w, inventory_items i, users u 
WHERE w.name = 'Main Warehouse' 
AND i.sku = 'ITEM1001' 
AND u.username = 'admin_user';

-- Insert Alerts for Warehouse Capacity
INSERT INTO alerts (id, warehouse_id, message, status) 
SELECT uuid_generate_v4(), w.id, 'Warehouse nearing capacity!', 'NEW' 
FROM warehouse w WHERE w.name = 'Main Warehouse';
