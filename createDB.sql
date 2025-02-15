-- Drop all tables first
DROP TABLE IF EXISTS 
    user_roles, 
    warehouse_inventory, 
    inventory_movement, 
    warehouse_personnel, 
    alerts, 
    warehouse_storage_locations, 
    inventory_items, 
    storage_locations, 
    warehouse, 
    users, 
    roles 
CASCADE;

-- Drop ENUM types if they exist
DROP TYPE IF EXISTS movement_type_enum, personnel_status_enum, alert_status_enum CASCADE;


-- Enable UUID extension (needed for UUID primary keys)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- User Table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Role Table
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role_name VARCHAR(50) UNIQUE NOT NULL
);

-- User Roles (Many-to-Many)
CREATE TABLE user_roles (
    user_role_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID REFERENCES roles(id) ON DELETE CASCADE
);

-- Warehouse Table
CREATE TABLE warehouse (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) UNIQUE NOT NULL,
    location VARCHAR(255) NOT NULL,
    max_capacity INT NOT NULL CHECK (max_capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Storage Locations
CREATE TABLE storage_locations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) UNIQUE NOT NULL,
    max_capacity INT CHECK (max_capacity > 0)
);

-- Warehouse Storage Locations (Many-to-Many)
CREATE TABLE warehouse_storage_locations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    storage_location_id UUID REFERENCES storage_locations(id) ON DELETE CASCADE
);

-- Inventory Items
CREATE TABLE inventory_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    storage_location UUID REFERENCES storage_locations(id) ON DELETE SET NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Warehouse Inventory (Many-to-Many)
CREATE TABLE warehouse_inventory (
    warehouse_inventory_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    item_id UUID REFERENCES inventory_items(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity >= 0),
    UNIQUE (warehouse_id, item_id) -- Prevents duplicate warehouse-item entries
);

-- Inventory Movement Log
CREATE TYPE movement_type_enum AS ENUM ('ADD', 'REMOVE', 'TRANSFER');

CREATE TABLE inventory_movement (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    item_id UUID REFERENCES inventory_items(id) ON DELETE CASCADE,
    from_warehouse UUID REFERENCES warehouse(id) ON DELETE SET NULL,
    to_warehouse UUID REFERENCES warehouse(id) ON DELETE SET NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    movement_type movement_type_enum NOT NULL,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id UUID REFERENCES users(id) ON DELETE SET NULL
);

-- Warehouse Personnel (Users assigned to Warehouses)
-- Create the ENUM type first
CREATE TYPE personnel_status_enum AS ENUM ('ACTIVE', 'INACTIVE');

-- Now use the ENUM type in the table definition
CREATE TABLE warehouse_personnel (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    status personnel_status_enum DEFAULT 'ACTIVE'
);


-- Alerts Table (For warehouse capacity alerts)
CREATE TYPE alert_status_enum AS ENUM ('NEW', 'IN PROGRESS', 'RESOLVED');

CREATE TABLE alerts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID REFERENCES warehouse(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    status alert_status_enum DEFAULT 'NEW',
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
