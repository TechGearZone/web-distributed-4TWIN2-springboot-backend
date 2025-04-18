-- Insert Orders
INSERT INTO orders (id, order_number, order_date, status, user_id, total_amount, shipping_address, billing_address, payment_method)
VALUES
(1, 'ORD123', NOW(), 'CREATED', 101, 500, '123 Street, City', '123 Street, City', 'CREDIT_CARD'),
(2, 'ORD124', NOW(), 'SHIPPED', 102, 1200, '456 Avenue, City', '456 Avenue, City', 'PAYPAL');

-- Insert Order Items
INSERT INTO order_items (id, order_id, product_id, product_name, quantity, price)
VALUES
(1, 1, 201, 'Laptop', 1, 500),
(2, 2, 202, 'Smartphone', 2, 600);
