CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_items_service_type_id ON cart_items(service_type_id);
CREATE INDEX idx_orders_cart_id ON orders(cart_id);
CREATE INDEX idx_invoices_order_id ON invoices(order_id);
