CREATE TABLE IF NOT EXISTS products (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255)  NOT NULL CHECK (char_length(name) > 0),
    description VARCHAR(4000),
    price      NUMERIC(12, 2) NOT NULL CHECK (price > 0),
    stock      INTEGER       NOT NULL CHECK (stock >= 0),
    category   VARCHAR(100)  NOT NULL CHECK (char_length(category) > 0),
    status     VARCHAR       NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'ARCHIVED')),
    created_at TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_products_status ON products (status);

INSERT INTO products (name, description, price, stock, category, status) VALUES
    ('Дорожный утюг', 'Компактный утюг. Подойдет для поездок.', 1499.99, 50, 'Бытовая техника', 'ACTIVE'),
    ('Пылесос года', 'Лучший пылесос в 2026 году!', 5000.0, 120, 'Бытовая техника', 'ACTIVE');
