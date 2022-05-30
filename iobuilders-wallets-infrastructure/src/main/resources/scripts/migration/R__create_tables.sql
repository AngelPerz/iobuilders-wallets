CREATE TABLE IF NOT EXISTS wallets (
    id VARCHAR NOT NULL,
    owner VARCHAR NOT NULL,
    alias VARCHAR,
    balance NUMERIC NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS movements (
    id SERIAL,
    origin_wallet VARCHAR NOT NULL,
    destiny_wallet VARCHAR NOT NULL,
    amount NUMERIC,
    request_time TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (origin_wallet) REFERENCES wallets(id),
    FOREIGN KEY (destiny_wallet) REFERENCES wallets(id)
);

CREATE TABLE IF NOT EXISTS deposits (
    id SERIAL,
    wallet_id VARCHAR NOT NULL,
    amount NUMERIC,
    request_time TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);