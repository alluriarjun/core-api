CREATE TABLE users (
    id           BIGSERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE stocks (
    id         BIGSERIAL PRIMARY KEY,
    symbol     VARCHAR(20)  NOT NULL,
    exchange   VARCHAR(20)  NOT NULL,
    name       VARCHAR(255),
    sector     VARCHAR(100),
    currency   VARCHAR(10)  NOT NULL DEFAULT 'USD',
    asset_type VARCHAR(20)  NOT NULL DEFAULT 'EQUITY',
    UNIQUE (symbol, exchange)
);

CREATE TABLE watchlists (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, name)
);

CREATE TABLE watchlist_items (
    id           BIGSERIAL PRIMARY KEY,
    watchlist_id BIGINT      NOT NULL REFERENCES watchlists(id) ON DELETE CASCADE,
    stock_id     BIGINT      NOT NULL REFERENCES stocks(id)     ON DELETE CASCADE,
    added_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (watchlist_id, stock_id)
);