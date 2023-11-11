--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS "user"
(
    id            BIGSERIAL PRIMARY KEY,
    nickname      VARCHAR(40) NOT NULL UNIQUE,
    password      TEXT        NOT NULL,
    register_time TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS role
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(20) NOT NULL UNIQUE,
    description text        NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT REFERENCES "user" (id),
    role_id BIGINT REFERENCES role (id),
    PRIMARY KEY (user_id, role_id)
);
