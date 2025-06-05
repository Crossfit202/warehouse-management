-- Drop all tables first
DROP TABLE IF EXISTS 
    warehouse_inventory, 
    inventory_movement, 
    warehouse_personnel, 
    alerts, 
    warehouse_storage_locations, 
    inventory_items, 
    storage_locations, 
    warehouse, 
    users 
CASCADE;

-- Drop ENUM types if they exist
DROP TYPE IF EXISTS movement_type_enum, role_enum, personnel_status_enum, alert_status_enum CASCADE;

-- Enable UUID extension (needed for UUID primary keys)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- User Table (role as VARCHAR now)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL DEFAULT 'USER',  -- ✅ ENUM removed; now VARCHAR
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add index for quick user lookup
CREATE INDEX idx_users_username ON users(username);

-- Warehouse Table
CREATE TABLE warehouse (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) UNIQUE NOT NULL,
    location VARCHAR(255) NOT NULL,
    max_capacity INT NOT NULL CHECK (max_capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER users_update_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER warehouse_update_timestamp
BEFORE UPDATE ON warehouse
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Storage Locations Table (generic templates like "Large Shelf")
CREATE TABLE storage_locations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) UNIQUE NOT NULL,
    max_capacity INT CHECK (max_capacity > 0)
);

-- Warehouse Storage Locations (INSTANCE of a template per warehouse)
-- ✅ CHANGED: this is now a true entity, not just a join table
CREATE TABLE warehouse_storage_locations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    storage_location_template_id UUID REFERENCES storage_locations(id) ON DELETE CASCADE,  -- ✅ Renamed column
    name VARCHAR(255) NOT NULL,  -- ✅ e.g., "Large Shelf A1"
    max_capacity INT CHECK (max_capacity >= 0),  -- ✅ Optional: override capacity
    current_capacity INT DEFAULT 0               -- ✅ Optional: track live usage
);

-- Inventory Items Table
CREATE TABLE inventory_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Add index for fast SKU lookups
CREATE INDEX idx_inventory_items_sku ON inventory_items(sku);

-- Warehouse Inventory (Many-to-Many)
CREATE TABLE warehouse_inventory (
    warehouse_inventory_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    warehouse_storage_location_id UUID REFERENCES warehouse_storage_locations(id) ON DELETE CASCADE,
    item_id UUID REFERENCES inventory_items(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity >= 0),
    UNIQUE (warehouse_id, item_id)
);

-- Inventory Movement Log (movement_type as VARCHAR)
CREATE TABLE inventory_movement (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    item_id UUID REFERENCES inventory_items(id) ON DELETE CASCADE,
    from_warehouse UUID REFERENCES warehouse(id) ON DELETE SET NULL,
    to_warehouse UUID REFERENCES warehouse(id) ON DELETE SET NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    movement_type VARCHAR(255) NOT NULL, -- ✅ ENUM removed; now VARCHAR
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id UUID REFERENCES users(id) ON DELETE SET NULL
);

-- Warehouse Personnel (status as VARCHAR)
CREATE TABLE warehouse_personnel (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    status VARCHAR(255) DEFAULT 'ACTIVE' -- ✅ ENUM removed; now VARCHAR
);

-- Add indexes for warehouse personnel relationships
CREATE INDEX idx_warehouse_personnel_user ON warehouse_personnel(user_id);
CREATE INDEX idx_warehouse_personnel_warehouse ON warehouse_personnel(warehouse_id);

-- Alerts Table (status as VARCHAR)
CREATE TABLE alerts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    status VARCHAR(255) DEFAULT 'NEW', -- ✅ ENUM removed; now VARCHAR
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
