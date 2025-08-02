-- infra/postgres/init.sql
-- DROP TABLE IF EXISTS trades;
-- DROP TABLE IF EXISTS ratings;
-- DROP TABLE IF EXISTS instruments;
-- DROP TABLE IF EXISTS currencies;
-- DROP TABLE IF EXISTS clients;

-- Create Clients table
CREATE TABLE IF NOT EXISTS clients (
    client_id VARCHAR(255) PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_info VARCHAR(255)
);

-- Create Currencies table
CREATE TABLE IF NOT EXISTS currencies (
    ticker VARCHAR(10) PRIMARY KEY,
    currency_code VARCHAR(3) NOT NULL,
    currency_name VARCHAR(255)
);

-- Create Instruments table
CREATE TABLE IF NOT EXISTS instruments (
    ticker VARCHAR(10) PRIMARY KEY,
    instrument_name VARCHAR(255),
    instrument_type VARCHAR(50)
);

-- Create Ratings table
CREATE TABLE IF NOT EXISTS ratings (
    ticker VARCHAR(10) PRIMARY KEY,
    rating_value VARCHAR(50) NOT NULL,
    rating_description VARCHAR(255),
    rating_agency VARCHAR(255)
);

-- Create Trades table
CREATE TABLE IF NOT EXISTS trades (
    log_id SERIAL PRIMARY KEY,
    trade_id UUID NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    quantity DECIMAL(18, 4) NOT NULL,
    price DECIMAL(18, 4) NOT NULL,
    buy_sell VARCHAR(4) NOT NULL, 
    rating_value VARCHAR(50),
    rating_description VARCHAR(255),
    rating_agency VARCHAR(255),
    instrument_name VARCHAR(255),
    instrument_type VARCHAR(50),
    currency_code VARCHAR(3),
    currency_name VARCHAR(255),
    client_id VARCHAR(255),
    client_name VARCHAR(255),
    validation_status VARCHAR(255), -- e.g., 'VALIDATED', 'INVALID'
    settlement_status VARCHAR(255), -- e.g., 'SETTLED', 'PENDING
    -- 'BUY' or 'SELL'
    lifecycle_state VARCHAR(50) NOT NULL, -- e.g., 'CAPTURED', 'ENRICHED', 'SETTLED'

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Add some initial reference data (optional, but good for testing)
INSERT INTO currencies (ticker, currency_code, currency_name) VALUES
    ('GOOG', 'USD', 'United States Dollar'),
    ('IBM', 'USD', 'United States Dollar'),
    ('MSFT', 'USD', 'United States Dollar'),
    ('AAPL', 'USD', 'United States Dollar'),
    ('TSLA', 'USD', 'United States Dollar'),
    ('UL', 'JPY', 'Japanese Yen'),
    ('WMT', 'EUR', 'Euro')
ON CONFLICT (ticker) DO UPDATE SET
    currency_name = EXCLUDED.currency_name;

INSERT INTO instruments (ticker, instrument_name, instrument_type) VALUES
    ('GOOG', 'Alphabet Inc.', 'Tech Stock'),
    ('IBM', 'International Business Machines Corporation', 'Tech Stock'),
    ('MSFT', 'Microsoft Corporation', 'Tech Stock'),
    ('AAPL', 'Apple Inc.', 'Tech Stock'),
    ('TSLA', 'Tesla Inc.', 'Automotive Stock'),
    ('UL', 'Unilever PLC', 'Consumer Goods Stock'),
    ('WMT', 'Walmart Inc.', 'Retail Stock')
ON CONFLICT (ticker) DO UPDATE SET
    instrument_name = EXCLUDED.instrument_name,
    instrument_type = EXCLUDED.instrument_type;

INSERT INTO ratings (ticker, rating_value, rating_description, rating_agency) VALUES
    ('GOOG', 'AAA', 'Highest quality, lowest risk','S&P'),
    ('IBM', 'AA', 'High quality, low risk', 'S&P'),
    ('MSFT', 'A+', 'Upper medium quality, low risk', 'S&P'),
    ('AAPL', 'A', 'Upper medium quality, moderate risk', 'S&P'),
    ('TSLA', 'BBB+', 'Medium quality, moderate risk', 'S&P'),
    ('UL', 'BBB', 'Medium quality, moderate risk', 'Moodys'),
    ('WMT', 'BBB-', 'Medium quality, moderate risk', 'Fitch')
ON CONFLICT (ticker) DO UPDATE SET
    rating_description = EXCLUDED.rating_description;

INSERT INTO clients (client_id, client_name, address, contact_info) VALUES
    ('client_Trial', 'Client A', '123 Main St', 'clienta@example.com'),
    ('client_Mitali', 'Client B', '456 Oak Ave', 'clientb@example.com')
ON CONFLICT (client_id) DO UPDATE SET
    client_name = EXCLUDED.client_name,
    address = EXCLUDED.address,
    contact_info = EXCLUDED.contact_info;