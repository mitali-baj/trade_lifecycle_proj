-- infra/postgres/init.sql

-- Create Clients table
CREATE TABLE IF NOT EXISTS clients (
    client_id UUID PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_info VARCHAR(255)
);

-- Create Currencies table
CREATE TABLE IF NOT EXISTS currencies (
    currency_id UUID PRIMARY KEY,
    currency_code VARCHAR(3) UNIQUE NOT NULL,
    currency_name VARCHAR(255)
);

-- Create Instruments table
CREATE TABLE IF NOT EXISTS instruments (
    instrument_id UUID PRIMARY KEY,
    ticker VARCHAR(10) UNIQUE NOT NULL,
    instrument_name VARCHAR(255),
    instrument_type VARCHAR(50)
);

-- Create Ratings table
CREATE TABLE IF NOT EXISTS ratings (
    rating_id UUID PRIMARY KEY,
    rating_value VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

-- Create Trades table
CREATE TABLE IF NOT EXISTS trades (
    trade_id UUID PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL,
    quantity DECIMAL(18, 4) NOT NULL,
    price DECIMAL(18, 4) NOT NULL,
    buy_sell VARCHAR(4) NOT NULL, -- 'BUY' or 'SELL'
    lifecycle_state VARCHAR(50) NOT NULL, -- e.g., 'CAPTURED', 'ENRICHED', 'SETTLED'
    
    -- Foreign Keys (initially nullable, enriched later)
    client_id UUID,
    currency_id UUID,
    instrument_id UUID,
    rating_id UUID,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (currency_id) REFERENCES currencies(currency_id),
    FOREIGN KEY (instrument_id) REFERENCES instruments(instrument_id),
    FOREIGN KEY (rating_id) REFERENCES ratings(rating_id)
);

-- Add some initial reference data (optional, but good for testing)
INSERT INTO currencies (currency_id, currency_code, currency_name) VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'USD', 'United States Dollar'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'EUR', 'Euro')
ON CONFLICT (currency_code) DO UPDATE SET
    currency_name = EXCLUDED.currency_name;

INSERT INTO instruments (instrument_id, ticker, instrument_name, instrument_type) VALUES
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'AAPL', 'Apple Inc.', 'Equity'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'GOOGL', 'Alphabet Inc.', 'Equity')
ON CONFLICT (ticker) DO UPDATE SET
    instrument_name = EXCLUDED.instrument_name,
    instrument_type = EXCLUDED.instrument_type;

INSERT INTO ratings (rating_id, rating_value, description) VALUES
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'AAA', 'Highest quality, lowest risk'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'BBB', 'Medium quality, moderate risk')
ON CONFLICT (rating_value) DO UPDATE SET
    description = EXCLUDED.description;

INSERT INTO clients (client_id, client_name, address, contact_info) VALUES
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Client A', '123 Main St', 'clienta@example.com'),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Client B', '456 Oak Ave', 'clientb@example.com')
ON CONFLICT (client_id) DO UPDATE SET
    client_name = EXCLUDED.client_name,
    address = EXCLUDED.address,
    contact_info = EXCLUDED.contact_info;
