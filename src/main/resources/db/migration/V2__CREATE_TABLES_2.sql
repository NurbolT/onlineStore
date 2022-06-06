-- Таблица категории
CREATE TABLE category
(
    id         BIGSERIAL                           NOT NULL PRIMARY KEY,
    title      VARCHAR(150)                        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Таблица продукты
CREATE TABLE product
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    title       VARCHAR(150) NOT NULL,
    description TEXT         NOT NULL,
    price       FLOAT        NOT NULL,
    status      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    category_id BIGINT REFERENCES category (id) ON DELETE CASCADE
);

CREATE TABLE order_details
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    user_id    BIGINT                NOT NULL,
    total      FLOAT                 NOT NULL,
    created_at TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_data (id) ON DELETE CASCADE
);

CREATE TABLE order_items
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    order_id   BIGINT                NOT NULL,
    product_id BIGINT                NOT NULL,
    created_at TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES order_details (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);