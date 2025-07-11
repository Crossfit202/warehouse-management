SELECT * FROM users;

DELETE FROM users
WHERE username = 'clerk_user';

DELETE FROM users
WHERE username = 'manager_user';

DELETE FROM users
WHERE username = 'admin_user';

UPDATE users
SET role = 'ROLE_ADMIN'
WHERE username = 'Jonathan';

SELECT * FROM warehouse;

SELECT * FROM alerts;

SELECT * FROM inventory_items;

SELECT * FROM inventory_movement;

SELECT * FROM storage_locations;

SELECT * FROM warehouse_inventory;

UPDATE warehouse_inventory
SET min_quantity = '20'
WHERE warehouse_inventory_id = '346bc232-966c-41ea-ae5b-17b0bf1253d6';

SELECT * FROM warehouse_personnel;

INSERT INTO warehouse_personnel (user_id, warehouse_id)
SELECT u.id, w.id
FROM users u, warehouse w
WHERE u.username = 'Jonathan'
  AND w.name = 'Main Warehouse';


SELECT * FROM warehouse_storage_locations;

TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Insert dummy users into the users table

INSERT INTO users (id, username, email, password, role, created_at, updated_at)
VALUES
    (uuid_generate_v4(), 'ADMIN', 'admin@google.com', 
     '$2y$12$q/7EfJ0/748n.H9uwVLEnOEz.MYS2mWbH8iFrII5DTfoDs9Jne7D6', -- admin@1234
     'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (uuid_generate_v4(), 'manager_user', 'manager@example.com', 
     '$2y$12$bgJoCCfF58d0C4JBnbKvA.JMakjT5xlwosLct1PY3TbrkyWclvRnO', -- manager@1234
     'MANAGER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (uuid_generate_v4(), 'clerk_user', 'clerk@example.com', 
     '$2y$12$BU/sYX8GdrOBnqo1mQ6wyOlZsCJaNI4TG9bC7N8Vq7ISLorSO6AtS', -- clerk@1234
     'INV_CLERK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (uuid_generate_v4(), 'regular_user', 'user@example.com', 
     '$2y$12$CNijbLm4xYw77gIxRrpHwuZx0rSQX5XptzcOj/9mVj4V5R5l9IqiS', -- user@1234
     'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

DELETE FROM users
WHERE username = 'avni';
