-- Таблица пользователей
CREATE TABLE user_data
(
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    email        VARCHAR(50)  NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    password     VARCHAR(150) NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Таблица ролей
CREATE TABLE role_data
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    name      VARCHAR(200) NOT NULL
);

-- Таблица связи юзеров и ролей
CREATE TABLE user_role
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    user_id    BIGINT REFERENCES user_data (id) ON DELETE CASCADE,
    role_id    BIGINT REFERENCES role_data (id) ON DELETE CASCADE
);