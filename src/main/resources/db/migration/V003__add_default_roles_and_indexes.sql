-- Insert default roles if they don't exist
INSERT IGNORE INTO roles (name) VALUES 
('ADMIN'),
('BUS_OPERATOR'), 
('CUSTOMER'),
('STAFF');

-- Create indexes for performance if they don't exist
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_contracts_email ON contracts(email);
CREATE INDEX IF NOT EXISTS idx_contracts_status ON contracts(status);
CREATE INDEX IF NOT EXISTS idx_bus_operators_email ON bus_operators(email);
CREATE INDEX IF NOT EXISTS idx_bus_operators_owner ON bus_operators(owner_id);
