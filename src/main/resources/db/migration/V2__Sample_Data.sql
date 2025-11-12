-- V2__Sample_Data.sql
-- Sample data for testing and development

-- Insert sample admin user (password: admin123)
INSERT INTO users (full_name, email, password, phone_number, role, enabled)
VALUES ('Admin User', 'admin@coffeeshop.com', 
        '$2a$10$xqQxvMpGN5XtN6yUDKqHN.ZaKxP67TqH3aEexB5lSUKDNKc3VCH0K',
        '+1234567890', 'ADMIN', true);

-- Insert sample customer user (password: customer123)
INSERT INTO users (full_name, email, password, phone_number, role, enabled)
VALUES ('John Doe', 'customer@example.com',
        '$2a$10$xqQxvMpGN5XtN6yUDKqHN.ZaKxP67TqH3aEexB5lSUKDNKc3VCH0K',
        '+1234567891', 'CUSTOMER', true);

-- Insert sample products
INSERT INTO products (name, description, price, image_url, category, available, is_featured, prep_time_minutes, calories) VALUES
-- Hot Coffee
('Espresso', 'Rich and bold espresso shot', 3.50, 'https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04', 'ESPRESSO', true, true, 3, 10),
('Americano', 'Espresso with hot water', 4.00, 'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd', 'HOT_COFFEE', true, true, 4, 15),
('Cappuccino', 'Espresso with steamed milk and foam', 4.50, 'https://images.unsplash.com/photo-1572442388796-11668a67e53d', 'CAPPUCCINO', true, true, 5, 120),
('Latte', 'Espresso with steamed milk', 4.75, 'https://images.unsplash.com/photo-1541167760496-1628856ab772', 'LATTE', true, true, 5, 150),
('Mocha', 'Chocolate espresso with steamed milk', 5.25, 'https://images.unsplash.com/photo-1578374173703-9b6c0e7e71f3', 'MOCHA', true, false, 6, 290),
('Macchiato', 'Espresso with a dollop of foam', 4.25, 'https://images.unsplash.com/photo-1557006021-b85faa2bc5e2', 'MACCHIATO', true, false, 4, 80),

-- Iced Coffee
('Iced Americano', 'Espresso over ice with cold water', 4.50, 'https://images.unsplash.com/photo-1517487881594-2787fef5ebf7', 'ICED_COFFEE', true, true, 3, 15),
('Iced Latte', 'Espresso with cold milk over ice', 5.00, 'https://images.unsplash.com/photo-1461023058943-07fcbe16d735', 'ICED_COFFEE', true, true, 4, 140),
('Cold Brew', 'Smooth cold-brewed coffee', 4.75, 'https://images.unsplash.com/photo-1517487881594-2787fef5ebf7', 'ICED_COFFEE', true, false, 2, 5),

-- Frappuccino
('Caramel Frappuccino', 'Blended coffee with caramel', 6.00, 'https://images.unsplash.com/photo-1572490122747-3968b75cc699', 'FRAPPUCCINO', true, true, 6, 420),
('Mocha Frappuccino', 'Blended mocha coffee drink', 6.00, 'https://images.unsplash.com/photo-1572788370641-e762d46e779a', 'FRAPPUCCINO', true, false, 6, 410),

-- Tea
('Green Tea', 'Fresh brewed green tea', 3.00, 'https://images.unsplash.com/photo-1556679343-c7306c1976bc', 'TEA', true, false, 5, 0),
('Chai Latte', 'Spiced tea with steamed milk', 4.50, 'https://images.unsplash.com/photo-1597318112693-2c9f8f4d2f71', 'TEA', true, false, 5, 200),

-- Smoothies
('Berry Smoothie', 'Mixed berry smoothie', 5.50, 'https://images.unsplash.com/photo-1505252585461-04db1eb84625', 'SMOOTHIE', true, false, 4, 250),
('Mango Smoothie', 'Fresh mango smoothie', 5.50, 'https://images.unsplash.com/photo-1600271886742-f049cd451bba', 'SMOOTHIE', true, false, 4, 230),

-- Pastries
('Croissant', 'Buttery, flaky croissant', 3.50, 'https://images.unsplash.com/photo-1555507036-ab1f4038808a', 'PASTRY', true, false, 0, 231),
('Blueberry Muffin', 'Fresh blueberry muffin', 3.25, 'https://images.unsplash.com/photo-1607958996333-41aef7caefaa', 'PASTRY', true, false, 0, 426),
('Chocolate Chip Cookie', 'Homemade chocolate chip cookie', 2.75, 'https://images.unsplash.com/photo-1499636136210-6f4ee915583e', 'DESSERT', true, false, 0, 280);

-- Update products with some ratings
UPDATE products SET rating = 4.8, review_count = 127 WHERE name = 'Espresso';
UPDATE products SET rating = 4.6, review_count = 89 WHERE name = 'Cappuccino';
UPDATE products SET rating = 4.7, review_count = 156 WHERE name = 'Latte';
UPDATE products SET rating = 4.9, review_count = 203 WHERE name = 'Caramel Frappuccino';
UPDATE products SET rating = 4.5, review_count = 78 WHERE name = 'Iced Latte';
